package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.sql.Date;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-01
 */
public class DataSourceTO extends NamedEntityTO {
    
    @Serial
    private static final long serialVersionUID = 8715798341643627122L;
    
    private final String description;
    
    private final String baseURL;
    
    private final Date releaseDate;
    
    private final String releaseVersion;
    
    public DataSourceTO(Integer id, String name, String description, String baseURL, Date releaseDate, String releaseVersion)
            throws IllegalArgumentException {
        super(id, name);
        this.description = description;
        this.baseURL = baseURL;
        this.releaseDate = releaseDate;
        this.releaseVersion = releaseVersion;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getBaseURL() {
        return baseURL;
    }
    
    public Date getReleaseDate() {
        return releaseDate;
    }
    
    public String getReleaseVersion() {
        return releaseVersion;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", DataSourceTO.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("name='" + getName() + "'")
                .add("description='" + description + "'")
                .add("baseURL='" + baseURL + "'")
                .add("releaseDate=" + releaseDate)
                .add("releaseVersion='" + releaseVersion + "'")
                .toString();
    }
}
