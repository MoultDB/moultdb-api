package org.moultdb.importer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author Valentine Rech de Laval
 * @since 2022-01-18
 */
public class GeologicalAgeImporterTest {
    
    private final static Logger logger = LogManager.getLogger(GeologicalAgeImporterTest.class.getName());
    private static final String ICS_JSON_FILE = "/precambrian.json";
    
    @Test
    void testGson() throws IOException {
        String file = this.getClass().getResource(ICS_JSON_FILE).getFile();
        String content = new String(Files.readAllBytes(Paths.get(file)));
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, Object>  icsGeologicalAge = gson.fromJson(content,  Map.class);
        logger.debug(icsGeologicalAge.toString());
    
    }
}
