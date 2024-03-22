package org.moultdb.importer;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.moultdb.importer.genomics.GeneBean;
import org.moultdb.importer.genomics.GeneParser;
import org.moultdb.importer.genomics.OrthogroupBean;

import java.util.Set;

class GeneParserTest {
    
    private final static Logger logger = LogManager.getLogger(GeneParserTest.class.getName());
    private static final String REFSEQ_TSV = "/fake-refseq-genes.tsv";
    private static final String GENBANK_TSV = "/fake-genebank-genes.tsv";
    private static final String ORTHOGROUPS_TSV = "/fake-orthogroups.tsv";
    
    @Test
    public void test() {
        Gson gson = new Gson();
        GeneParser parser = new GeneParser();
        
        String file = this.getClass().getResource(REFSEQ_TSV).getFile();
        Set<GeneBean> geneBeans = parser.parseGeneFile(file);
        logger.debug(gson.toJson(geneBeans));
        
        file = this.getClass().getResource(GENBANK_TSV).getFile();
        geneBeans = parser.parseGeneFile(file);
        logger.debug(gson.toJson(geneBeans));
        
        file = this.getClass().getResource(ORTHOGROUPS_TSV).getFile();
        Set<OrthogroupBean> orthogroupBeans = parser.parseOrthogroupFile(file);
        logger.debug(gson.toJson(orthogroupBeans));
    }
}