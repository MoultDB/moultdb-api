package org.moultdb.api.model;

import java.util.Date;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-01
 */
public class DataSource {
    
    private final String name;
    private final String description;
    private final String baseURL;
    private final Date lastImportDate;
    private final String releaseVersion;
    
    public DataSource(String name, String description, String baseURL, Date lastImportDate, String releaseVersion)
            throws IllegalArgumentException {
        this.name = name;
        this.description = description;
        this.baseURL = baseURL;
        this.lastImportDate = lastImportDate;
        this.releaseVersion = releaseVersion;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getBaseURL() {
        return baseURL;
    }
    
    public Date getLastImportDate() {
        return lastImportDate;
    }
    
    public String getReleaseVersion() {
        return releaseVersion;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", DataSource.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .add("baseURL='" + baseURL + "'")
                .add("lastImportDate=" + lastImportDate)
                .add("releaseVersion='" + releaseVersion + "'")
                .toString();
    }
}
