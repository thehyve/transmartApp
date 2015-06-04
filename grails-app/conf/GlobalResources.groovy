import grails.util.Environment

def envSwitch = { devValue, otherValue ->
    Environment.current == Environment.DEVELOPMENT ? devValue : otherValue
}

modules = {
    main_mod { // Stuff used by the main layout (layouts/main.gsp)
        dependsOn 'jquery, extjs'

        resource url: 'images/searchtool.ico'
        resource url: 'css/main.css'


    }

    extjs {
        dependsOn 'jquery'
        resource url: 'js/ext/resources/css/ext-all.css'
        resource url: 'js/ext/resources/css/xtheme-gray.css'
        resource url: 'js/ext/adapter/jquery/ext-jquery-adapter.js', disposition: 'head'
        resource url: 'js/ext/' + envSwitch('ext-all-debug.js', 'ext-all.js'), disposition: 'head'
    }
}
