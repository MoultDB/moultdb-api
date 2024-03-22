package org.moultdb.importer;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.moultdb.importer.genomics.DomainBean;
import org.moultdb.importer.genomics.DomainParser;

import java.util.Set;

class DomainParserTest {
    
    private final static Logger logger = LogManager.getLogger(DomainParserTest.class.getName());
    private static final String DOMAIN_TSV = "/fake-domains.tsv";
    
    @Test
    public void test() {
        Gson gson = new Gson();
        DomainParser parser = new DomainParser();
        
        String file = this.getClass().getResource(DOMAIN_TSV).getFile();
        Set<DomainBean> domainBeans = parser.parseDomainFile(file);
        logger.debug(gson.toJson(domainBeans));
    }
}