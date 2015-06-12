import org.apache.log4j.Logger
import org.codehaus.groovy.grails.commons.spring.DefaultBeanConfiguration
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.access.AccessDeniedHandlerImpl
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler

// plugin is not functional at this point
//import org.springframework.security.extensions.kerberos.web.SpnegoAuthenticationProcessingFilter
import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy
import org.springframework.security.web.session.ConcurrentSessionFilter
import org.transmart.authorization.CurrentUserBeanFactoryBean
import org.transmart.authorization.CurrentUserBeanProxyFactory
import org.transmart.authorization.QueriesResourceAuthorizationDecorator
import org.transmartproject.security.AuthSuccessEventListener
import org.transmartproject.security.BadCredentialsEventListener
import org.transmartproject.security.BruteForceLoginLockService

def logger = Logger.getLogger('com.recomdata.conf.resources')

beans = {
    xmlns context: "http://www.springframework.org/schema/context"

    if (grailsApplication.config.org.transmart.security.samlEnabled) {
        importBeans('classpath:/spring/spring-security-saml.xml')
    }

    /* core-api authorization wrapped beans */
    queriesResourceAuthorizationDecorator(QueriesResourceAuthorizationDecorator) {
        DefaultBeanConfiguration bean ->
            bean.beanDefinition.autowireCandidate = false
    }

    "${CurrentUserBeanProxyFactory.BEAN_BAME}"(CurrentUserBeanProxyFactory)
    "${CurrentUserBeanProxyFactory.SUB_BEAN_REQUEST}"(CurrentUserBeanFactoryBean) { bean ->
        bean.scope = 'request'
    }

    legacyQueryResultAccessCheckRequestCache(
            QueriesResourceAuthorizationDecorator.LegacyQueryResultAccessCheckRequestCache) { bean ->
        bean.scope = 'request'
    }

    sessionRegistry(SessionRegistryImpl)
    sessionAuthenticationStrategy(ConcurrentSessionControlStrategy, sessionRegistry) {
        maximumSessions = 10
    }
    concurrentSessionFilter(ConcurrentSessionFilter) {
        sessionRegistry = sessionRegistry
        expiredUrl = '/login'
    }

    redirectStrategy(DefaultRedirectStrategy)
    accessDeniedHandler(AccessDeniedHandlerImpl) {
        errorPage = '/login'
    }
    failureHandler(SimpleUrlAuthenticationFailureHandler) {
        defaultFailureUrl = '/login'
    }

    //overrides bean implementing GormUserDetailsService?
    userDetailsService(com.recomdata.security.AuthUserDetailsService)

    if (grailsApplication.config.org.transmart.security.spnegoEnabled) {
        // plugin is not functional at this point
//        SpnegoAuthenticationProcessingFilter(SpnegoAuthenticationProcessingFilter) {
//            authenticationManager = ref('authenticationManager')
//            failureHandler = ref('failureHandler')
//        }

        ldapUserDetailsMapper(com.recomdata.security.LdapAuthUserDetailsMapper) {
            dataSource = ref('dataSource')
            springSecurityService = ref('springSecurityService')
            databasePortabilityService = ref('databasePortabilityService')
        }

    } else {
        // plugin is not functional at this point
//        SpringSecurityKerberosGrailsPlugin.metaClass.getDoWithSpring = {->
//            logger.info "Skipped Kerberos Grails plugin initialization"
//            return {}
//        }
        SpringSecurityLdapGrailsPlugin.metaClass.getDoWithSpring = { ->
            logger.info "Skipped LDAP Grails plugin initialization"
            return {}
        }
    }

    if (!('clientCredentialsAuthenticationProvider' in
            grailsApplication.config.grails.plugin.springsecurity.providerNames)) {
        SpringSecurityOauth2ProviderGrailsPlugin.metaClass.getDoWithSpring = { ->
            logger.info "Skipped Oauth2 Grails plugin initialization (doWithSpring)"
            return {}
        }
        SpringSecurityOauth2ProviderGrailsPlugin.metaClass.getDoWithApplicationContext = { ->
            logger.info "Skipped Oauth2 Grails plugin initialization (doWithApplicationContext)"
            return {}
        }
    }

    bruteForceLoginLockService(BruteForceLoginLockService) {
        allowedNumberOfAttempts = grailsApplication.config.bruteForceLoginLock.allowedNumberOfAttempts
        lockTimeInMinutes = grailsApplication.config.bruteForceLoginLock.lockTimeInMinutes
    }

    authSuccessEventListener(AuthSuccessEventListener) {
        bruteForceLoginLockService = ref('bruteForceLoginLockService')
    }

    badCredentialsEventListener(BadCredentialsEventListener) {
        bruteForceLoginLockService = ref('bruteForceLoginLockService')
    }
}
