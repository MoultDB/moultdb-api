package org.moultdb.importer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.moultdb.importer.genomics.OrthogroupParser;

class OrthogroupParserTest {
    
    private final static Logger logger = LogManager.getLogger(OrthogroupParserTest.class.getName());
    private static final String ORTHOGROUPS_TSV = "/fake-orthogroups.tsv";
    
    @Test
    public void test() {
        String file = this.getClass().getResource(ORTHOGROUPS_TSV).getFile();
        
        OrthogroupParser parser = new OrthogroupParser();
        parser.getOrthogroupBeans(file);
    }
}