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
  
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.war.file = "transmart.war"

//grails.plugin.location.rmodules = "C:\\git\\jnjtransmart\\Rmodules"

//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        //mavenLocal()
        mavenCentral()
        mavenRepo([
                name: 'repo.theyve.nl-public',
                root: 'http://repo.thehyve.nl/content/repositories/public/',
        ])
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
		compile 'axis:axis:1.4'  
    }
	
	plugins {
		runtime ":hibernate:$grailsVersion"
		runtime ":jquery:1.7.1"
		runtime ":resources:1.1.6"
		runtime ":spring-security-core:1.2.7.3"
		runtime ":quartz:1.0-RC2"
		runtime ":prototype:1.0"
		compile ":rdc-rmodules:0.2", {
			/* depends on quartz 0.4.2. Even though ivy says it's evicted, when
			 * grails refresh-dependencies is run, it tries to "upgrade" 1.0-RC2
			 * to this 0.4.2 version gotten from the plugin */
			exclude 'quartz'
		}
		build ":tomcat:$grailsVersion"
	}
}
