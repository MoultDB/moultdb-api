package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.sql.Date;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-01
 */
public class DataSourceTO extends NamedEntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = 8715798341643627122L;
    
    private final String baseURL;
    private final Date lastImportDate;
    private final String releaseVersion;
    
    public DataSourceTO(Integer id, String name, String description, String baseURL,
                        Date lastImportDate, String releaseVersion)
            throws IllegalArgumentException {
        super(id, name, description);
        this.baseURL = baseURL;
        this.lastImportDate = lastImportDate;
        this.releaseVersion = releaseVersion;
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
        return new StringJoiner(", ", DataSourceTO.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("name='" + getName() + "'")
                .add("description='" + getDescription() + "'")
                .add("baseURL='" + baseURL + "'")
                .add("lastImportDate=" + lastImportDate)
                .add("releaseVersion='" + releaseVersion + "'")
                .toString();
    }
}
