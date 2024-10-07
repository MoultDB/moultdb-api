package org.moultdb.importer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.moultdb.importer.taxonannotation.TaxonAnnotationBean;
import org.moultdb.importer.taxonannotation.TaxonAnnotationParser;

import java.util.List;
import java.util.Map;

class TaxonAnnotationParserTest {
    
    private final static Logger logger = LogManager.getLogger(TaxonAnnotationParserTest.class.getName());
    private static final String FOSSIL_ANNOTATIONS_TSV = "/fake-taxon-annotations.tsv";
    private static final String DEV_STAGE_MAPPING_TSV = "/fake-dev-stage-mapping.tsv";
    
    @Test
    public void testTaxonAnnotationParser() {
        String file = this.getClass().getResource(FOSSIL_ANNOTATIONS_TSV).getFile();
        
        TaxonAnnotationParser parser = new TaxonAnnotationParser();
        List<TaxonAnnotationBean> taxonAnnotationBeans = parser.parseAnnotations(file);
        logger.info(taxonAnnotationBeans.toString());
    }
    
    @Test
    public void testMappingTaxonAnnotationParser() {
        String mappingFile = this.getClass().getResource(DEV_STAGE_MAPPING_TSV).getFile();
        
        TaxonAnnotationParser parser = new TaxonAnnotationParser();
        Map<String, String> mapping = parser.parseDevStageMapping(mappingFile);
        logger.info(mapping.toString());
    }
}