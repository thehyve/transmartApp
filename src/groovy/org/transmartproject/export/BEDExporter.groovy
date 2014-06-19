package org.transmartproject.export

import groovy.sql.Sql
import groovy.transform.TupleConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.transmartproject.core.dataquery.DataRow
import org.transmartproject.core.dataquery.TabularResult
import org.transmartproject.core.dataquery.highdim.AssayColumn
import org.transmartproject.core.dataquery.highdim.HighDimensionResource
import org.transmartproject.core.dataquery.highdim.projections.Projection

import javax.annotation.PostConstruct

@TupleConstructor class BEDEntry {
    String bed_c1
    int bed_c2
    int bed_c3
    String bed_c4
    String bed_c6
    int bed_c7
    int bed_c8
    int bed_c10
    String bed_c11
    String bed_c12
}

@Component
class BEDExporter implements HighDimExporter {

    def dataSource

    @Autowired
    HighDimensionResource highDimensionResourceService
    
    @Autowired
    HighDimExporterRegistry highDimExporterRegistry
    
    @PostConstruct
    void init() {
        this.highDimExporterRegistry.registerHighDimensionExporter(
                format, this )
    }
    
    @Override
    public boolean isDataTypeSupported(String dataType) {
        return dataType == "protein"
    }

    @Override
    public String getFormat() {
        return "BED"
    }

    @Override
    public String getDescription() {
        return "BED format for genome browser"
    }

    @Override
    public void export(TabularResult tabularResult, Projection projection,
            OutputStream outputStream) {
        export( tabularResult, projection, outputStream, { false } )
    }
            
    @Override
    public void export(TabularResult tabularResult, Projection projection,
            OutputStream outputStream, Closure isCancelled) {

        BEDExporter.log.info("started exporting to $format ")
        def startTime = System.currentTimeMillis()

        if (isCancelled() ) {
            return
        }

        def uniprotNameToGeneSymbol = [:]
        def sql = new Sql(dataSource)
        sql.eachRow("""
            SELECT biomart.bio_marker.bio_marker_name, deapp.de_protein_annotation.uniprot_id
            FROM biomart.bio_marker
              RIGHT JOIN biomart.bio_marker_correl_mv ON (biomart.bio_marker.bio_marker_id = biomart.bio_marker_correl_mv.asso_bio_marker_id)
              RIGHT JOIN deapp.de_protein_annotation ON (biomart.bio_marker_correl_mv.bio_marker_id = deapp.de_protein_annotation.biomarker_id)
            WHERE biomart.bio_marker_correl_mv.correl_type = 'PROTEIN TO GENE'
              AND biomart.bio_marker.bio_marker_name != 'NA';
            """.toString()) { row ->
            uniprotNameToGeneSymbol.put(row.uniprot_id, row.bio_marker_name)
        }

        if (isCancelled() ) {
            return
        }

        def geneSymbolToBED = [:]
        sql.eachRow("""
            SELECT bed_c1, bed_c2, bed_c3, bed_c4, bed_c6, bed_c7, bed_c8, bed_c10, bed_c11, bed_c12, gene_symbol
            FROM biomart.bio_bed_entries
            WHERE biomart.bio_bed_entries.gene_symbol in (${uniprotNameToGeneSymbol.values().collect { '\'' + it + '\'' }.join(',') });
            """.toString()) { row ->
            if (geneSymbolToBED.containsKey(row.gene_symbol)) {
                geneSymbolToBED.get(row.gene_symbol) << new BEDEntry(row.bed_c1, row.bed_c2, row.bed_c3, row.bed_c4, row.bed_c6, row.bed_c7, row.bed_c8, row.bed_c10, row.bed_c11, row.bed_c12)
            }
            else {
                geneSymbolToBED.put(row.gene_symbol, [new BEDEntry(row.bed_c1, row.bed_c2, row.bed_c3, row.bed_c4, row.bed_c6, row.bed_c7, row.bed_c8, row.bed_c10, row.bed_c11, row.bed_c12)])
            }
        }

        if (tabularResult.rowsDimensionLabel=='Proteins') {
        }

        outputStream.withWriter( "UTF-8" ) { writer ->

            List<AssayColumn> assayList = tabularResult.indicesList

            // Start looping
            writer << "track" + "\t" + "name = tranSMART export" + "\t" + "description = " + "\t" + "url = "

            writeloop:
            for (DataRow datarow : tabularResult) {
                // Test periodically if the export is cancelled
                if (isCancelled() ) {
                    return
                }
                def bedEntries = geneSymbolToBED.get(uniprotNameToGeneSymbol.get(datarow.uniprotId))
                bedEntries.each {
                    writer << it.bed_c1 + "\t" + it.bed_c2 + "\t" + it.bed_c3 + "\t" + it.bed_c4 + "\t" + datarow.data.sum()/datarow.data.size() + "\t" +
                            it.bed_c6 + "\t" + it.bed_c7 + "\t" + it.bed_c8 + "\t" + "0" + "\t" + it.bed_c10 + "\t" + it.bed_c11 + "\t" + it.bed_c12 + "\n"
                }

            }
        }
        
        BEDExporter.log.info("Exporting data took ${System.currentTimeMillis() - startTime} ms")
    }
    
    @Override
    public String getProjection() {
        Projection.DEFAULT_REAL_PROJECTION
    }

}
