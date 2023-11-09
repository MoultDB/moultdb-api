package org.moultdb.api.model;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-01
 */
public class DbXref {
    
    private final String accession;
    
    private final String name;
    
    private final DataSource dataSource;
    
    public DbXref(String accession, String name, DataSource dataSource) {
        this.accession = accession;
        this.name = name;
        this.dataSource = dataSource;
    }
    
    public String getAccession() {
        return accession;
    }
    
    public String getName() {
        return name;
    }
    
    public DataSource getDataSource() {
        return dataSource;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DbXref dbXref = (DbXref) o;
        return Objects.equals(accession, dbXref.accession)
                && Objects.equals(name, dbXref.name)
                && Objects.equals(dataSource, dbXref.dataSource);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(accession, name, dataSource);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", DbXref.class.getSimpleName() + "[", "]")
                .add("accession='" + accession + "'")
                .add("name='" + name + "'")
                .add("dataSource=" + dataSource)
                .toString();
    }
}
