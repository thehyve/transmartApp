package org.transmartproject.search.indexing

interface FacetsIndexingModule {

    String getName()

    Set<String> getSupportedDocumentTypes()

    Set<FacetsDocId> fetchAllIds(String type)

    Set<FacetsDocument> collectDocumentsWithIds(Set<FacetsDocId> docIds)

    FacetsFieldDisplaySettings getDisplaySettingsForIndex(String fieldName /* solr schema field name */)
}
