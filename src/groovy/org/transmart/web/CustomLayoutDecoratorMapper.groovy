package org.transmart.web

import com.opensymphony.module.sitemesh.Decorator
import com.opensymphony.module.sitemesh.Page
import org.codehaus.groovy.grails.web.sitemesh.GrailsLayoutDecoratorMapper
import org.codehaus.groovy.grails.web.sitemesh.GrailsNoDecorator

import javax.servlet.http.HttpServletRequest

/**
 * Automatically applies the ajax.gsp layout to ajax requests without a layout.
 */
class CustomLayoutDecoratorMapper extends GrailsLayoutDecoratorMapper {

    public static final String REQUEST_WITH_HEADER = 'X-Requested-With'

    @Override
    Decorator getDecorator(HttpServletRequest request, Page page) {
        Decorator parentDecorator = super.getDecorator(request, page)
        if (parentDecorator != null && !(parentDecorator instanceof GrailsNoDecorator)) {
            return parentDecorator
        }


        def headerValue = request.getHeader(REQUEST_WITH_HEADER)
        if (headerValue == 'XMLHttpRequest') {
            getNamedDecorator(request, 'ajax.gsp')
        } else {
            parentDecorator
        }
    }
}
