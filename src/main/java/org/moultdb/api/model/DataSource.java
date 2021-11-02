package org.moultdb.api.model;

import java.util.Date;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-01
 */
public class DataSource extends NamedEntity {
    
    private final String description;
    
    private final String baseURL;
    
    private final Date releaseDate;
    
    private final String releaseVersion;
    
    public DataSource(String name, String description, String baseURL, Date releaseDate, String releaseVersion)
            throws IllegalArgumentException {
        super(name);
        this.description = description;
        this.baseURL = baseURL;
        this.releaseDate = releaseDate;
        this.releaseVersion = releaseVersion;
    }
}
