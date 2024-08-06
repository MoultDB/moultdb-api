package org.moultdb.importer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.moultdb.importer.taxonannotation.TaxonAnnotationBean;
import org.moultdb.importer.taxonannotation.TaxonAnnotationParser;

import java.util.List;

class TaxonAnnotationParserTest {
    
    private final static Logger logger = LogManager.getLogger(TaxonAnnotationParserTest.class.getName());
    private static final String FOSSIL_ANNOTATIONS_TSV = "/fake-taxon-annotations.tsv";
    
    @Test
    public void test() {
        String file = this.getClass().getResource(FOSSIL_ANNOTATIONS_TSV).getFile();
        
        TaxonAnnotationParser parser = new TaxonAnnotationParser();
        List<TaxonAnnotationBean> taxonAnnotationBeans = parser.parseAnnotations(file);
        logger.info(taxonAnnotationBeans.toString());
    }

}