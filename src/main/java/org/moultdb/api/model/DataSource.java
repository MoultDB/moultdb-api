package org.moultdb.api.model;

import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-01
 */
public record DataSource(String name, String shortName, String description, String baseURL, String xrefURL,
                         Date lastImportDate, String releaseVersion, Integer displayOrder) {
    
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
