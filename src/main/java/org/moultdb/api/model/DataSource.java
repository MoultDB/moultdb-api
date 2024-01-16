package org.moultdb.api.model;

import java.util.Date;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-01
 */
public class DataSource {
    
    public final static String X_REF_TAG = "[xref_acc]";
    
    private final String name;
    private final String shortName;
    private final String description;
    private final String baseURL;
    private final String xrefURL;
    private final Date lastImportDate;
    private final String releaseVersion;
    
    public DataSource(String name, String shortName, String description, String baseURL, String xrefURL,
                      Date lastImportDate, String releaseVersion)
            throws IllegalArgumentException {
        this.name = name;
        this.shortName = shortName;
        this.description = description;
        this.baseURL = baseURL;
        this.xrefURL = xrefURL;
        this.lastImportDate = lastImportDate;
        this.releaseVersion = releaseVersion;
    }
    
    public String getName() {
        return name;
    }
    
    public String getShortName() {
        return shortName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getBaseURL() {
        return baseURL;
    }
    
    public String getXrefURL() {
        return xrefURL;
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
                .add("shortName='" + shortName + "'")
                .add("description='" + description + "'")
                .add("baseURL='" + baseURL + "'")
                .add("xrefURL='" + xrefURL + "'")
                .add("lastImportDate=" + lastImportDate)
                .add("releaseVersion='" + releaseVersion + "'")
                .toString();
    }
}
