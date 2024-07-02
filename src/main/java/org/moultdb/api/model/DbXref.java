package org.moultdb.api.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.Objects;
import java.util.StringJoiner;

import static org.moultdb.api.model.DataSource.X_REF_TAG;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-01
 */
public class DbXref {
    
    protected static final Comparator<DbXref> DBXREF_COMPARATOR = Comparator.comparing(DbXref::isMain, Comparator.nullsFirst(Comparator.reverseOrder()))
            .thenComparing(xref -> xref.getDataSource().getName(), Comparator.nullsFirst(Comparator.reverseOrder()))
            .thenComparing(DbXref::getAccession, Comparator.nullsFirst(Comparator.naturalOrder()));
    
    private final String accession;
    
    private final String name;
    
    private final DataSource dataSource;
    
    private final Boolean main;
    
    public DbXref(String accession, String name, DataSource dataSource, Boolean main) {
        this.accession = accession;
        this.name = name;
        this.dataSource = dataSource;
        this.main = main;
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
    
    public String getXrefURL() {
        if (StringUtils.isBlank(this.getAccession())) {
            return null;
        }
        String xRefUrl = this.getDataSource().getXrefURL();
        if (StringUtils.isBlank(xRefUrl)) {
            return null;
        }
        if (xRefUrl.contains(X_REF_TAG)) {
            xRefUrl = xRefUrl.replace(X_REF_TAG, this.getAccession());
        }
        return xRefUrl;
    }
    
    public Boolean isMain() {
        return main;
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
                .add("main=" + main)
                .toString();
    }
}
