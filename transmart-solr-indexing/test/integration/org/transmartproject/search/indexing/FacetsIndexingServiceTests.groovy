package org.transmartproject.search.indexing

import com.google.common.collect.ImmutableMultimap
import grails.test.mixin.TestMixin
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.common.SolrInputDocument
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.springframework.beans.factory.annotation.Autowired
import org.transmartproject.db.test.RuleBasedIntegrationTestMixin

import static org.transmartproject.search.indexing.FacetsIndexingService.FIELD_NAME_ID

@TestMixin(RuleBasedIntegrationTestMixin)
class FacetsIndexingServiceTests {

    public static final int TOTAL_NUMBER_OF_DOCUMENTS = 14
    public static final int NUMBER_OF_FILE_DOCUMENTS = 1

    @Autowired
    SolrFacetsCore solrFacetsCore

    @Autowired
    FacetsIndexingService indexingService

    @Rule
    public TestRule skipIfSolrNotAvailableRule = new SkipIfSolrNotAvailableRule()

    @Before
    void before() {
        indexingService.clearIndex()
    }

    @Test
    void testFullIndex() {
        indexingService.fullIndex()

        assert countDocuments('*:*') == TOTAL_NUMBER_OF_DOCUMENTS
    }

    @Test
    void testIndexByTypes() {
        indexingService.indexByTypes(['FILE'] as Set)

        assert countDocuments('*:*') == NUMBER_OF_FILE_DOCUMENTS
        assert countDocuments('TYPE:FILE') == NUMBER_OF_FILE_DOCUMENTS
    }

    @Test
    void testIndexByIds() {
        indexingService.indexByIds([new FacetsDocId('FOLDER:1992454')] as Set)

        assert countDocuments('*:*') == 1
        assert countDocuments('folder_subtype_s:STUDY') == 1
    }

    @Test
    void testStuffIsMergedFromDifferentModules() {
        String id = 'FOLDER:1992454'
        indexingService.indexByIds([new FacetsDocId(id)] as Set)

        assert countDocuments("id:\"$id\" AND tag_number_of_followed_subjects_i:19 AND design_s:Observational") == 1
    }

    @Test
    void addDocument() {
        String id = 'FOO:12345'

        indexingService.addDocument(new FacetsDocument(
                facetsDocId: new FacetsDocId(id),
                fieldValues: ImmutableMultimap.of(),
        ))
        indexingService.flush()

        assert countDocuments("id:\"$id\"") == 1
    }

    @Test
    void testRemoveDocument() {
        def id = new FacetsDocId('FOO:12345')

        indexingService.addDocument(new FacetsDocument(
                facetsDocId: id,
                fieldValues: ImmutableMultimap.of(),
        ))
        indexingService.flush()

        indexingService.removeDocuments([id] as Set)
        indexingService.flush()

        assert countDocuments('id:"FOO:12345"') == 0
    }


    private int countDocuments(obj) {
        SolrQuery q = new SolrQuery(obj)
        q.set('rows', 0)
        solrFacetsCore.query(q).results.numFound
    }
}
