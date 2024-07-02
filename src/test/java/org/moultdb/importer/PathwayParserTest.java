package org.moultdb.importer;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.moultdb.importer.genomics.PathwayBean;
import org.moultdb.importer.genomics.PathwayCvBean;
import org.moultdb.importer.genomics.PathwayParser;

import java.util.Set;

class PathwayParserTest {
    
    private final static Logger logger = LogManager.getLogger(PathwayParserTest.class.getName());
    private static final String PATHWAYS_TSV = "/fake-pathways.tsv";
    private static final String PATHWAY_CV_TSV = "/fake-pathway-cv.tsv";
    
    @Test
    public void test() {
        Gson gson = new Gson();
        PathwayParser parser = new PathwayParser();
        
        String file = this.getClass().getResource(PATHWAYS_TSV).getFile();
        Set<PathwayBean> pathwayBeans = parser.parsePathwayFile(file);
        logger.debug(gson.toJson(pathwayBeans));

        file = this.getClass().getResource(PATHWAY_CV_TSV).getFile();
        Set<PathwayCvBean> pathwayCvBeans = parser.parsePathwayCvFile(file);
        logger.debug(gson.toJson(pathwayCvBeans));
    }
}