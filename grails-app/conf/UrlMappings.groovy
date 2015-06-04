class UrlMappings {
    static mappings = {
        "/$controller/$action?/$id?"()
        '/'(controller: 'home', action: 'index')
        '500'(view: '/error')
    }
}
