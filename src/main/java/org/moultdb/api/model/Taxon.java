package org.moultdb.api.model;

import java.util.*;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-14
 */
public class Taxon extends NamedEntity<String> {
    
    private final Comparator<DbXref> DBXREF_COMPARATOR = Comparator.comparing(DbXref::isMain, Comparator.nullsFirst(Comparator.reverseOrder()))
            .thenComparing(xref -> xref.getDataSource().getName(), Comparator.nullsFirst(Comparator.reverseOrder()))
            .thenComparing(DbXref::getAccession, Comparator.nullsFirst(Comparator.naturalOrder()));
    
    private final String commonName;
    private final Boolean extinct;
    private final Set<DbXref> dbXrefs;
    private final DbXref mainDbXref;
    
    public Taxon(String path, String scientificName, String commonName,
                 Boolean extinct, Collection<DbXref> dbXrefs) {
        super(path, scientificName);
        this.commonName = commonName;
        this.extinct = extinct;
        this.dbXrefs =  Collections.unmodifiableSet(dbXrefs == null ?
                new HashSet<>(): new HashSet<>(dbXrefs));
        this.mainDbXref =  dbXrefs == null ? null : dbXrefs.stream().sorted(DBXREF_COMPARATOR).toList().get(0);
    }
    
    public String getPath() {
        return getId();
    }
    
    public String getScientificName() {
        return getName();
    }
    
    public String getCommonName() {
        return commonName;
    }
    
    public Boolean isExtinct() {
        return extinct;
    }
    
    public Set<DbXref> getDbXrefs() {
        return dbXrefs;
    }
    
    public DbXref getMainDbXref() {
        return mainDbXref;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Taxon.class.getSimpleName() + "[", "]")
                .add("path='" + getPath() + "'")
                .add("scientificName='" + getScientificName() + "'")
                .add("commonName='" + commonName + "'")
                .add("extinct=" + extinct)
                .add("dbXrefs=" + dbXrefs)
                .add("mainDbXref=" + mainDbXref)
                .toString();
    }
}
