package org.transmartproject.search.indexing

import com.google.common.collect.ImmutableSet
import com.google.common.collect.Sets
import grails.plugin.cache.ehcache.GrailsEhcacheCacheManager
import groovy.util.logging.Log4j
import net.sf.ehcache.Cache
import net.sf.ehcache.CacheException
import net.sf.ehcache.Ehcache
import net.sf.ehcache.Status
import net.sf.ehcache.config.CacheConfiguration
import net.sf.ehcache.loader.CacheLoader
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.request.LukeRequest
import org.apache.solr.client.solrj.response.LukeResponse
import org.apache.solr.client.solrj.response.QueryResponse
import org.apache.solr.common.luke.FieldFlag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import org.transmartproject.search.indexing.modules.AbstractFacetsIndexingFolderModule

import javax.annotation.PostConstruct

import static org.apache.solr.client.solrj.response.LukeResponse.FieldInfo.parseFlags
import static org.transmartproject.search.indexing.FacetsFieldImpl.getDefaultDisplayName

@Component
@Log4j
class FacetsQueryingService {

    private static final String ALL_DISPLAY_SETTINGS_CACHE_KEY = 'allDisplaySettings'
    private static final String TOP_TERMS_CACHE_KEY = 'topTerms'

    private static final String FACETS_QUERYING_SERVICE_CACHE = 'FacetsIndexCache'

    @Autowired
    private List<FacetsIndexingModule> modules

    @Autowired
    private SolrFacetsCore server

    @Autowired
    private GrailsEhcacheCacheManager grailsCacheManager


    private CacheLoader cacheLoader = new FacetsQueryingCacheLoader()

    private static final Set<String> BLACKLISTED_FIELD_NAMES =
            ImmutableSet.of(
                    FacetsIndexingService.FIELD_NAME_ID,
                    FacetsIndexingService.FIELD_NAME_TYPE,
                    FacetsIndexingService.FIELD_NAME_FILE_PATH,
                    FacetsIndexingService.FIELD_NAME_FOLDER_ID,
                    FacetsIndexingService.FIELD_NAME__VERSION_,
                    AbstractFacetsIndexingFolderModule.FOLDER_DOC_TYPE
            )

    void clearCaches() {
        ehcache.removeAll()
    }

    LinkedHashMap<String, SortedSet<TermCount>> getTopTerms() {
        ehcache.getWithLoader(TOP_TERMS_CACHE_KEY, cacheLoader, null).objectValue
    }

    LinkedHashMap<String, FacetsFieldDisplaySettings> getAllDisplaySettings() {
        ehcache.getWithLoader(ALL_DISPLAY_SETTINGS_CACHE_KEY, cacheLoader, null).objectValue
    }

    List<String> getAllFacetFields() {
        // only non-hidden _s fields
        allDisplaySettings.findAll { e ->
            e.key =~ /_s\z/ && !e.value.hideFromListings
        }
        .sort { it.value }
        .collect { it.key }
    }

    // leak solr api details, no point abstracting it
    QueryResponse query(SolrQuery query) {
        server.query(query)
    }

    LinkedHashMap<String, SortedSet<TermCount>> parseFacetCounts(QueryResponse response) {
        response.facetFields.collectEntries {
            [it.name,
             Sets.newTreeSet(it.values.collect {
                 v -> new TermCount(term: v.name, count: v.count)
             })]
        }
    }

    private LinkedHashMap<String, SortedSet<TermCount>> fetchTopTerms() {
        log.info('Going to calculate top terms')

        def q = new SolrQuery('*:*')
        q.addFacetField(*allFacetFields)
        q.rows = 0
        parseFacetCounts(server.query(q))
    }

    private LinkedHashMap<String, FacetsFieldDisplaySettings> fetchAllDisplaySettings() {
        log.info('Going to determine all display settings')

        def req = new LukeRequest(numTerms: 0)
        LukeResponse lukeResponse = req.process(server.solrServer)

        lukeResponse.fieldInfo
                .entrySet()
                .findAll {
            !(it.key in BLACKLISTED_FIELD_NAMES)
        }
        .findAll { Map.Entry<String, LukeResponse.FieldInfo> e ->
            parseFlags(e.value.schema).contains(FieldFlag.INDEXED)
        }
        .collect { [it.key, getDisplaySettingsForField(it.key)] }
                .sort { a, b -> a[1] <=> b[1] }
                .collectEntries()
    }

    private FacetsFieldDisplaySettings getDisplaySettingsForField(String fieldName) {
        List<FacetsFieldDisplaySettings> allDisplaySettings = modules
                .collect { it.getDisplaySettingsForIndex fieldName }
                .findAll()

        if (!allDisplaySettings) {
            log.debug("Using default settings for field $fieldName")
            return new SimpleFacetsFieldDisplaySettings(
                    displayName: getDefaultDisplayName(fieldName),
                    hideFromListings: false,
                    order: Ordered.LOWEST_PRECEDENCE,
            )
        }

        new SimpleFacetsFieldDisplaySettings(
                displayName: (allDisplaySettings*.displayName as Set).join(' / '),
                hideFromListings: allDisplaySettings*.hideFromListings.every(),
                order: allDisplaySettings*.order.min(),
        )
    }

    private Ehcache getEhcache() {
        grailsCacheManager.getCache(FACETS_QUERYING_SERVICE_CACHE).nativeCache
    }

    class FacetsQueryingCacheLoader implements CacheLoader {

        @Override
        Object load(Object key) throws CacheException {
            if (key == ALL_DISPLAY_SETTINGS_CACHE_KEY) {
                fetchAllDisplaySettings()
            } else if (key == TOP_TERMS_CACHE_KEY) {
                fetchTopTerms()
            }
        }

        @Override
        Map loadAll(Collection keys) {
            keys.collectEntries {
                [it, load(it)]
            }
        }

        @Override
        Object load(Object key, Object argument) {
            load key
        }

        @Override
        Map loadAll(Collection keys, Object argument) {
            loadAll keys
        }

        @Override
        String getName() {
            'FacetsQueryingCacheLoader'
        }

        @Override
        CacheLoader clone(Ehcache cache) throws CloneNotSupportedException {
            throw new CloneNotSupportedException()
        }

        @Override
        void init() {}

        @Override
        void dispose() throws CacheException {}

        @Override
        Status getStatus() {
            Status.STATUS_ALIVE
        }
    }
}