package de

class DeVariantPopulationData {
    Integer id
    String chromosome
    Integer position
    String datasetId
    Float floatValue
    Integer integerValue
    String textValue
    Integer infoIndex
    String infoName

    static mapping = {
        table 'DE_VARIANT_POPULATION_DATA'
        version false
        id generator: 'assigned'
        columns {
            id column: 'VARIANT_POPULATION_DATA_ID'
            chromosome column: 'CHR'
            position column: 'POS'
        }
    }

    static def getSummaryMAF(datasetId) {
        //TODO JOIN rs_id field from de_variant_subject_detail
        findAllByDatasetId(datasetId)
    }
}