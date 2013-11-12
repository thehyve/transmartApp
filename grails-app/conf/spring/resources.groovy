/*************************************************************************
 * tranSMART - translational medicine data mart
 *
 * Copyright 2008-2012 Janssen Research & Development, LLC.
 *
 * This product includes software developed at Janssen Research & Development, LLC.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software  * Foundation, either version 3 of the License, or (at your option) any later version, along with the following terms:
 * 1.	You may convey a work based on this program in accordance with section 5, provided that you retain the above notices.
 * 2.	You may convey verbatim copies of this program code as you receive it, in any medium, provided that you retain the above notices.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS    * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 ******************************************************************/


import org.apache.log4j.Logger
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.extensions.kerberos.web.SpnegoAuthenticationProcessingFilter
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.access.AccessDeniedHandlerImpl
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy
import org.springframework.security.web.session.ConcurrentSessionFilter

def logger = Logger.getLogger('com.recomdata.conf.resources')

beans = {
    sessionRegistry(SessionRegistryImpl)
    sessionAuthenticationStrategy(ConcurrentSessionControlStrategy, sessionRegistry) {
        maximumSessions = 10
    }
    concurrentSessionFilter(ConcurrentSessionFilter) {
        sessionRegistry = sessionRegistry
        expiredUrl = '/login'
    }
    userDetailsService(com.recomdata.security.AuthUserDetailsService)

    redirectStrategy(DefaultRedirectStrategy)
    accessDeniedHandler(AccessDeniedHandlerImpl) {
        errorPage = '/login'
    }
    failureHandler(SimpleUrlAuthenticationFailureHandler) {
        defaultFailureUrl = '/login'
    }

    if (grailsApplication.config.org.transmart.security.spnegoEnabled) {
        SpnegoAuthenticationProcessingFilter(SpnegoAuthenticationProcessingFilter) {
            authenticationManager = ref('authenticationManager')
            failureHandler = ref('failureHandler')
        }
        ldapUserDetailsMapper(com.recomdata.security.CustomUserDetailsContextMapper) {
            dataSource = ref("dataSource")
        }
    } else {
        SpringSecurityKerberosGrailsPlugin.metaClass.getDoWithSpring = {->
            logger.info "Skipped Kerberos Grails plugin initialization"
            return {}
        }
        SpringSecurityLdapGrailsPlugin.metaClass.getDoWithSpring = {->
            logger.info "Skipped LDAP Grails plugin initialization"
            return {}
        }
    }
}
