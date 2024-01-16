package org.moultdb.api.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-14
 */
public class Taxon extends NamedEntity<String> {
    
    private final String commonName;
    private final Boolean extinct;
    private final Set<DbXref> dbXrefs;
    
    public Taxon(String path, String scientificName, String commonName,
                 Boolean extinct, Collection<DbXref> dbXrefs) {
        super(path, scientificName);
        this.commonName = commonName;
        this.extinct = extinct;
        this.dbXrefs =  Collections.unmodifiableSet(dbXrefs == null ?
                new HashSet<>(): new HashSet<>(dbXrefs));;
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
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Taxon.class.getSimpleName() + "[", "]")
                .add("path='" + getPath() + "'")
                .add("scientificName='" + getScientificName() + "'")
                .add("commonName='" + commonName + "'")
                .add("extinct=" + extinct)
                .add("dbXrefs=" + dbXrefs)
                .toString();
    }
}
