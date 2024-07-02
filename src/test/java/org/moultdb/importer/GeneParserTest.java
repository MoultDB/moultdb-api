package org.moultdb.importer;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.moultdb.importer.genomics.GeneBean;
import org.moultdb.importer.genomics.GeneParser;

import java.util.Set;

class GeneParserTest {
    
    private final static Logger logger = LogManager.getLogger(GeneParserTest.class.getName());
    private static final String REFSEQ_TSV = "/fake-refseq-genes.tsv";
    private static final String GENBANK_TSV = "/fake-genebank-genes.tsv";
    
    @Test
    public void test() {
        Gson gson = new Gson();
        GeneParser parser = new GeneParser();
        
        String file = this.getClass().getResource(REFSEQ_TSV).getFile();
        Set<GeneBean> geneBeans = parser.getGeneBeans(file);
        logger.debug(gson.toJson(geneBeans));
        
        file = this.getClass().getResource(GENBANK_TSV).getFile();
        geneBeans = parser.getGeneBeans(file);
        logger.debug(gson.toJson(geneBeans));
    }
}