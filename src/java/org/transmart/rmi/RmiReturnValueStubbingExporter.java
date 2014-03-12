package org.transmart.rmi;

import com.google.common.collect.Multimap;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.remoting.rmi.RmiInvocationHandler;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationTraceInterceptor;
import org.springframework.util.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

class RmiReturnValueStubbingExporter extends RmiServiceExporter {

    private Multimap<Class<?>, String> wrappedInterfaces;

    /* we can't use the Spring implementation of RmiInvocationHandler
     * because it's package protected */
    private class RmiReturnValueInvocationWrapper implements RmiInvocationHandler {
        private final Object wrappedObject;

        private RmiReturnValueInvocationWrapper(Class interf, Object wrappedObject) {
            this.wrappedObject = createProxyFor(interf, wrappedObject);
        }

        @Override
        public String getTargetInterfaceName() throws RemoteException {
            return null;
        }

        @Override
        public Object invoke(RemoteInvocation invocation) throws
                RemoteException, NoSuchMethodException, IllegalAccessException,
                InvocationTargetException {
            return RmiReturnValueStubbingExporter.this.invoke(
                    invocation, this.wrappedObject);
        }
    }

    @Override
    protected Object invoke(RemoteInvocation invocation, Object targetObject)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Object ret = maybeWrap(super.invoke(invocation, targetObject),
                invocation.getMethodName());

        if (ret instanceof Remote) {
            try {
                UnicastRemoteObject.exportObject((Remote) ret, 0);
            } catch (RemoteException e) {
                throw new InvocationTargetException(e);
            }
        }

        return ret;
    }

    private Object maybeWrap(Object superValue, String methodName) {
        for (Class interf: ClassUtils.getAllInterfacesForClass(superValue.getClass())) {
            if (wrappedInterfaces.get(interf).contains(methodName)) {
                return new RmiReturnValueInvocationWrapper(
                        interf, superValue);
            }
        }

        return superValue;
    }

    public void setWrappedInterfaces(Multimap<Class<?>, String> wrappedInterfaces) {
        this.wrappedInterfaces = wrappedInterfaces;
    }

    private Object createProxyFor(Class interf, Object object) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.addInterface(interf);
        proxyFactory.addAdvice(new RemoteInvocationTraceInterceptor(getExporterName()));

        proxyFactory.setTarget(object);
        // don't make opaque, on the client we need to know the interfaces implemented
        return proxyFactory.getProxy(getBeanClassLoader());
    }
}
