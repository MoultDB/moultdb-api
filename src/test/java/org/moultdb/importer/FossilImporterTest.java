package org.moultdb.importer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.moultdb.importer.fossilannotation.FossilAnnotationBean;
import org.moultdb.importer.fossilannotation.FossilImporter;

import java.util.List;

class FossilImporterTest {
    
    private final static Logger logger = LogManager.getLogger(FossilImporterTest.class.getName());
    private static final String FOSSIL_ANNOTATIONS_TSV = "/fake-fossil-annotations5.tsv";
    
    @Test
    public void test() {
        String file = this.getClass().getResource(FOSSIL_ANNOTATIONS_TSV).getFile();
        
        FossilImporter parser = new FossilImporter();
        List<FossilAnnotationBean> fossilAnnotationBeans = parser.parseFossilAnnotation(file);
        logger.info(fossilAnnotationBeans.toString());
    }

}