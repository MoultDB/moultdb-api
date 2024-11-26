package org.moultdb.api.model;

import java.util.Date;
import java.util.Objects;
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
    private final Integer displayOrder;
    
    public DataSource(String name, String shortName, String description, String baseURL, String xrefURL,
                      Date lastImportDate, String releaseVersion, Integer displayOrder)
            throws IllegalArgumentException {
        this.name = name;
        this.shortName = shortName;
        this.description = description;
        this.baseURL = baseURL;
        this.xrefURL = xrefURL;
        this.lastImportDate = lastImportDate;
        this.releaseVersion = releaseVersion;
        this.displayOrder = displayOrder;
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
    
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSource that = (DataSource) o;
        return Objects.equals(name, that.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
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
                .add("displayOrder=" + displayOrder)
                .toString();
    }
}
