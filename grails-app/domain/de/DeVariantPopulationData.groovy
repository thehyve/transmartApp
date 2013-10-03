package de

class DeVariantPopulationData {
    Integer id
    String chromosome
    Integer position
    String dataset_id
    Float float_value
    Integer integer_value
    String text_value
    Integer info_index
    String info_name

    static mapping = {
        table 'DE_VARIANT_POPULATION_DATA'
        version false
        id generator :'assigned'
        columns {
            id column:'VARIANT_POPULATION_DATA_ID'
            chromosome column:'CHR'
            position column:'POS'
        }
    }
}
