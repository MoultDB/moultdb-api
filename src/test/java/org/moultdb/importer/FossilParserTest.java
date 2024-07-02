package org.moultdb.importer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.moultdb.importer.fossilannotation.FossilAnnotationBean;
import org.moultdb.importer.fossilannotation.FossilParser;

import java.util.List;

class FossilParserTest {
    
    private final static Logger logger = LogManager.getLogger(FossilParserTest.class.getName());
    private static final String FOSSIL_ANNOTATIONS_TSV = "/fake-fossil-annotations5.tsv";
    
    @Test
    public void test() {
        String file = this.getClass().getResource(FOSSIL_ANNOTATIONS_TSV).getFile();
        
        FossilParser parser = new FossilParser();
        List<FossilAnnotationBean> fossilAnnotationBeans = parser.parseFossilAnnotation(file);
        logger.info(fossilAnnotationBeans.toString());
    }

}