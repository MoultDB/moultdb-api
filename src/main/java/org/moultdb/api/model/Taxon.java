package org.moultdb.api.model;

import java.util.*;

import static org.moultdb.api.model.DbXref.DBXREF_COMPARATOR;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-14
 */
public class Taxon extends NamedEntity<String> {
    
    private final String commonName;
    private final String rank;
    private final List<DbXref> dbXrefs;
    
    public Taxon(String path, String scientificName, String commonName,
                 String rank, Collection<DbXref> dbXrefs) {
        super(path, scientificName);
        this.commonName = commonName;
        this.rank = rank;
        this.dbXrefs = Collections.unmodifiableList(dbXrefs == null ?
                new ArrayList<>(): dbXrefs.stream().sorted(DBXREF_COMPARATOR).toList());
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
    
    public String getRank() {
        return rank;
    }
    
    public List<DbXref> getDbXrefs() {
        return dbXrefs;
    }
    
    public String getAccession() {
        if (!getDbXrefs().isEmpty()) {
            return dbXrefs.get(0).dataSource().shortName() + "/" + dbXrefs.get(0).accession();
        }
        return null;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Taxon.class.getSimpleName() + "[", "]")
                .add("accession=" + getAccession())
                .add("path='" + getPath() + "'")
                .add("scientificName='" + getScientificName() + "'")
                .add("commonName='" + commonName + "'")
                .add("rank=" + rank)
                .add("dbXrefs=" + dbXrefs)
                .toString();
    }
}
