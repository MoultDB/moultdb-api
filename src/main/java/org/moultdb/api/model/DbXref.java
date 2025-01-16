package org.moultdb.api.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-01
 */
public record DbXref(String accession, String name, DataSource dataSource, Boolean main) {
    
    public final static String X_REF_ACC_TAG = "[xref_acc]";
    
    protected static final Comparator<DbXref> DBXREF_COMPARATOR =
            Comparator.comparing(DbXref::main, Comparator.nullsFirst(Comparator.reverseOrder()))
                    .thenComparing(xref -> xref.dataSource().displayOrder(), Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(DbXref::accession, Comparator.nullsFirst(Comparator.naturalOrder()));
    
    public String xrefURL() {
        if (StringUtils.isBlank(this.accession())) {
            return null;
        }
        String xRefUrl = this.dataSource().xrefURL();
        if (StringUtils.isBlank(xRefUrl)) {
            return null;
        }
        if (xRefUrl.contains(X_REF_ACC_TAG)) {
            xRefUrl = xRefUrl.replace(X_REF_ACC_TAG, this.accession());
        }
        return xRefUrl;
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
