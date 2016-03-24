grails.cache.config = {
    cache {
        name 'FacetsQueryingServiceCache'
        eternal false
        timeToLiveSeconds(15 * 60)
        maxElementsInMemory 10
        maxElementsOnDisk 0
    }
}
