package org.moultdb.importer;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.moultdb.importer.genomics.OrthogroupBean;
import org.moultdb.importer.genomics.OrthogroupParser;

import java.util.Set;

class OrthogroupParserTest {
    
    private final static Logger logger = LogManager.getLogger(OrthogroupParserTest.class.getName());
    private static final String ORTHOGROUPS_TSV = "/fake-orthogroups.tsv";
    
    @Test
    public void test() {
        Gson gson = new Gson();
        OrthogroupParser parser = new OrthogroupParser();
        
        String file = this.getClass().getResource(ORTHOGROUPS_TSV).getFile();
        Set<OrthogroupBean> orthogroupBeans = parser.getOrthogroupBeans(file);
        logger.debug(gson.toJson(orthogroupBeans));
    }
}