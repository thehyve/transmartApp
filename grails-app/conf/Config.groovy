import grails.util.Environment


def console
if (!Environment.isWarDeployed() && Environment.isWithinShell()) {
    console = grails.build.logging.GrailsConsole.instance
} else {
    console = [
            info: { println "[INFO] $it" },
            warn: { println "[WARN] $it" },
    ]
}

/**
 * Running externalized configuration
 * Assuming the following configuration files
 * - in the executing user's home at ~/.grails/<app_name>Config/[Config.groovy|DataSource.groovy]
 * - config location set path by system variable '<APP_NAME>_CONFIG_LOCATION'
 * - dataSource location set path by system environment variable '<APP_NAME>_DATASOURCE_LOCATION'
 */

/* For some reason, the externalized config files are run with a different
 * binding. None of the variables appName, userHome, appVersion, grailsHome
 * are available; the binding will actually be the root config object.
 * So store the current binding in the config object so the externalized
 * config has access to the variables mentioned.
 */
org.transmart.originalConfigBinding = getBinding()

grails.config.locations = []
def defaultConfigFiles
if (Environment.current != Environment.TEST) {
    defaultConfigFiles = [
            "${userHome}/.grails/${appName}Config/Config.groovy",
            "${userHome}/.grails/${appName}Config/DataSource.groovy"
    ]
} else {
    // settings for the test environment
    org.transmart.configFine = true
}

defaultConfigFiles.each { filePath ->
    def f = new File(filePath)
    if (f.exists()) {
        grails.config.locations << "file:${filePath}"
    } else {
        console.info "Configuration file ${filePath} does not exist."
    }
}
String bashSafeEnvAppName = appName.toString().toUpperCase(Locale.ENGLISH).replaceAll(/-/, '_')

def externalConfig = System.getenv("${bashSafeEnvAppName}_CONFIG_LOCATION")
if (externalConfig) {
    grails.config.locations << "file:" + externalConfig
}
def externalDataSource = System.getenv("${bashSafeEnvAppName}_DATASOURCE_LOCATION")
if (externalDataSource) {
    grails.config.locations << "file:" + externalDataSource
}
grails.config.locations.each { console.info "Including configuration file [${it}] in configuration building." }

/*
 *  The following lines are copied from the previous COnfig.groovy
 *
 */

grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.types = [html         : [
        'text/html',
        'application/xhtml+xml'
],
                     xml          : [
                             'text/xml',
                             'application/xml'
                     ],
                     text         : 'text-plain',
                     js           : 'text/javascript',
                     rss          : 'application/rss+xml',
                     atom         : 'application/atom+xml',
                     css          : 'text/css',
                     csv          : 'text/csv',
                     all          : '*/*',
                     json         : [
                             'application/json',
                             'text/json'
                     ],
                     form         : 'application/x-www-form-urlencoded',
                     multipartForm: 'multipart/form-data',
                     jnlp         : 'application/x-java-jnlp-file'
]
// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
grails.converters.default.pretty.print = true

/* Keep pre-2.3.0 behavior */
grails.databinding.convertEmptyStringsToNull = false
grails.databinding.trimStrings = false

com.recomdata.search.autocomplete.max = 20
// default paging size
com.recomdata.search.paginate.max = 20
com.recomdata.search.paginate.maxsteps = 5
com.recomdata.admin.paginate.max = 20

// Used as a default by the out-of-tree Config.groovy
org.transmart.defaultLoginRedirect = '/'

org.transmart.security.spnegoEnabled = false
grails.plugin.springsecurity.useSecurityEventListener = true
bruteForceLoginLock {
    allowedNumberOfAttempts = 3
    lockTimeInMinutes = 10
}

log4j = {
    environments {
        test {
            warn 'org.codehaus.groovy.grails.commons.spring'
            warn 'org.codehaus.groovy.grails.domain.GrailsDomainClassCleaner'
            warn 'org.codehaus.groovy.grails.plugins.DefaultGrailsPluginManager' //info to show plugin versions
            warn 'org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsDomainBinder' //info to show joined-subclass indo

            root {
                info('stdout')
            }
        }
    }
}

grails.resources.modules = {} // modules defined in grails-app/conf

// Added by the Spring Security OAuth2 Provider plugin:
grails.plugin.springsecurity.oauthProvider.clientLookup.className = 'org.transmart.oauth2.Client'
grails.plugin.springsecurity.oauthProvider.authorizationCodeLookup.className = 'org.transmart.oauth2.AuthorizationCode'
grails.plugin.springsecurity.oauthProvider.accessTokenLookup.className = 'org.transmart.oauth2.AccessToken'
grails.plugin.springsecurity.oauthProvider.refreshTokenLookup.className = 'org.transmart.oauth2.RefreshToken'
