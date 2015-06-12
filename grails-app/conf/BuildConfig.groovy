def forkSettingsRun = [
        minMemory: 1536,
        maxMemory: 4096,
        maxPerm  : 384,
        debug    : false,
]
def forkSettingsOther = [
        minMemory: 256,
        maxMemory: 1024,
        maxPerm  : 384,
        debug    : false,
]

grails.project.fork = [
        test   : forkSettingsOther,
        run    : forkSettingsRun,
        war    : false,
        console: forkSettingsOther]

grails.project.war.file = "target/${appName}.war"

/* we need at least servlet-api 2.4 because of HttpServletResponse::setCharacterEncoding */
grails.servlet.version = "2.5"

grails.project.dependency.resolver = 'maven'

def dm, dmClass
try {
    dmClass = new GroovyClassLoader().parseClass(
            new File('../transmart-dev/DependencyManagement.groovy'))
} catch (Exception e) {
}
if (dmClass) {
    dm = dmClass.newInstance()
}

grails.project.dependency.resolution = {
    inherits('global') {}
    log 'warn'

    if (!dm) {
        repositories {
            grailsCentral()
            mavenCentral()

            mavenRepo 'https://repo.transmartfoundation.org/content/repositories/public/'
            mavenRepo 'https://repo.thehyve.nl/content/repositories/public/'
        }
    } else {
        dm.configureRepositories delegate
    }

    dependencies {
        // you can remove whichever you're not using
        runtime 'org.postgresql:postgresql:9.3-1100-jdbc4'
        runtime 'com.oracle:ojdbc7:12.1.0.1'

        compile 'org.transmartproject:transmart-core-api:1.2.2-SNAPSHOT'

        /* we need at least servlet-api 2.4 because of HttpServletResponse::setCharacterEncoding */
        compile "javax.servlet:servlet-api:$grails.servlet.version" /* delete from the WAR afterwards */

        /* for SAML authentication */
        compile('org.springframework.security.extensions:spring-security-saml2-core:1.0.0.RELEASE') {
            //excludes of spring securirty necessary because they are for an older version (3.1 branch)
            //also remove xercesImpl because it breaks tomcat and is not otherwise needed
            excludes 'spring-security-config', 'spring-security-core', 'spring-security-web', 'xercesImpl'
        }

        compile 'org.apache.poi:poi:3.12'

        // spring security version should be in sync with that brought with
        // grails-spring-security-core
        runtime 'org.springframework.security:spring-security-config:3.2.3.RELEASE',
                'org.springframework.security:spring-security-web:3.2.3.RELEASE', {
            transitive = false
        }

        test('junit:junit:4.11') {
            transitive = false /* don't bring hamcrest */
            export = false
        }

        test 'org.hamcrest:hamcrest-core:1.3',
                'org.hamcrest:hamcrest-library:1.3'

        test 'org.gmock:gmock:0.9.0-r435-hyve2', {
            transitive = false
        }

    }

    plugins {
        build ':release:3.0.1'
        build ':rest-client-builder:2.0.1'
        build ':tomcat:7.0.52.1'

        compile ':hibernate:3.6.10.16'
        // Not compatible with spring security 3.2 yet
        //compile ':spring-security-kerberos:0.1'
        compile ':spring-security-ldap:2.0-RC2'
        compile ':spring-security-core:2.0-RC5'
        compile ':spring-security-oauth2-provider:2.0-RC4'

        runtime ':jquery:1.11.1'
        runtime ':jquery-ui:1.10.4'
        runtime ':resources:1.2.14'

        // support for static code analysis - see codenarc.reports property below
        compile ":codenarc:0.21"

        if (!dm) {
            runtime ':transmart-core:1.2.2-SNAPSHOT'
            runtime ':transmart-mydas:0.1-SNAPSHOT'
            runtime ':transmart-rest-api:1.2.2-SNAPSHOT'

            test ':transmart-core-db-tests:1.2.2-SNAPSHOT'
        } else {
            dm.internalDependencies delegate
        }

        // Doesn't work with forked tests yet
        //test ":code-coverage:1.2.6"
    }
}

dm?.with {
    configureInternalPlugin 'runtime', 'transmart-core'
    configureInternalPlugin 'test', 'transmart-core-db-tests'
    configureInternalPlugin 'runtime', 'transmart-mydas'
    configureInternalPlugin 'runtime', 'transmart-rest-api'
}

dm?.inlineInternalDependencies grails, grailsSettings

grails.war.resources = { stagingDir ->
    delete(file: "${stagingDir}/WEB-INF/lib/servlet-api-${grails.servlet.version}.jar")
}

codenarc.reports = {
    TransmartAppReport('html') {
        outputFile = 'CodeNarc-transmartApp-Report.html'
        title = 'transmartApp Report'
    }
}

// vim: set et ts=4 sw=4:
