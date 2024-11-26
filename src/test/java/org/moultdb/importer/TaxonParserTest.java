package org.moultdb.importer;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.importer.taxon.TaxonBean;
import org.moultdb.importer.taxon.TaxonParser;

import java.util.Set;

class TaxonParserTest {
    
    private final static Logger logger = LogManager.getLogger(TaxonParserTest.class.getName());
    private static final String TAXA_CSV = "/fake-taxonomy.csv";
    
    @Test
    public void test() {
        String file = this.getClass().getResource(TAXA_CSV).getFile();
        
        TaxonParser parser = new TaxonParser();
        Set<TaxonBean> taxonBeans = parser.parseTaxonFile(file);
        Gson gson = new Gson();
        logger.debug(gson.toJson(taxonBeans));
        
        Set<TaxonTO> taxonTOs = parser.getTaxonTOs(taxonBeans);
        logger.debug(gson.toJson(taxonTOs));
        
    }
}