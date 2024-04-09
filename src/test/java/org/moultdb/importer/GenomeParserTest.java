package org.moultdb.importer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.moultdb.importer.genomics.GenomeBean;
import org.moultdb.importer.genomics.GenomeParser;

import java.util.Set;

class GenomeParserTest {
    
    private final static Logger logger = LogManager.getLogger(GenomeParserTest.class.getName());
    
    private static final String GENOMES_TSV = "/fake-genomes.tsv";
    
    @Test
    public void test() {
        String file = this.getClass().getResource(GENOMES_TSV).getFile();
        
        GenomeParser parser = new GenomeParser();
        Set<GenomeBean> genomeBeans = parser.parseGenomeFile(file);
        logger.info(genomeBeans.toString());
    }

}