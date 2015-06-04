modules = {
    admin {
        dependsOn 'jquery', 'jquery-ui', 'main_mod'

        resource url: '/css/admin.css'

        resource url: '/js/usergroup.js'
        resource url: '/js/utilitiesMenu.js'
    }
}
