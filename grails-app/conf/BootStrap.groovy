import grails.plugin.springsecurity.SecurityFilterPosition
import grails.plugin.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.exceptions.GrailsConfigurationException
import org.slf4j.LoggerFactory

class BootStrap {

    final static logger = LoggerFactory.getLogger(this)

    def securityContextPersistenceFilter

    def grailsApplication

    def OAuth2SyncService

    def init = { servletContext ->
        securityContextPersistenceFilter.forceEagerSessionCreation = true

        SpringSecurityUtils.clientRegisterFilter('concurrentSessionFilter',
                SecurityFilterPosition.CONCURRENT_SESSION_FILTER)

        if (grailsApplication.config.org.transmart.security.samlEnabled) {
            SpringSecurityUtils.clientRegisterFilter(
                    'metadataGeneratorFilter', SecurityFilterPosition.FIRST)
            SpringSecurityUtils.clientRegisterFilter(
                    'samlFilter', SecurityFilterPosition.BASIC_AUTH_FILTER)
        }

        if (!grailsApplication.config.org.transmart.configFine.is(true)) {
            logger.error("Something wrong happened parsing the externalized " +
                    "Config.groovy, because we could not find the " +
                    "configuration setting 'org.transmart.configFine " +
                    "set to true.\n" +
                    "Tip: on ~/.grails/transmartConfig, run\n" +
                    "groovy -e 'new ConfigSlurper().parse(new File(\"Config.groovy\").toURL())'\n" +
                    "to detect compile errors. Other errors can be detected " +
                    "with a breakpoing on the catch block in ConfigurationHelper::mergeInLocations().\n" +
                    "Alternatively, you can change the console logging settings by editing " +
                    "\$GRAILS_HOME/scripts/log4j.properties, adding a proper appender and log " +
                    "org.codehaus.groovy.grails.commons.cfg.ConfigurationHelper at level WARN")
            throw new GrailsConfigurationException("Configuration magic setting not found")
        }


        if ('clientCredentialsAuthenticationProvider' in
                grailsApplication.config.grails.plugin.springsecurity.providerNames) {
            OAuth2SyncService.syncOAuth2Clients()
        }
    }
}
