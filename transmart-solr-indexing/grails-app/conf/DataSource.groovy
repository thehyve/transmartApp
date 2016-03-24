hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
//    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
    singleSession = true // configure OSIV singleSession mode
}

// environment specific settings
environments {
    test {
        dataSource {
            driverClassName = 'org.postgresql.Driver'
            url = 'jdbc:postgresql://localhost:25432/postgres'
            username = 'postgres'
            password = 'password'
            dbCreate = 'none'
            logSql = true
            formatSql = true
        }
    }
}
