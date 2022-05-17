package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-18
 */
public class TaxonTO extends NamedEntityTO<String> {
    
    @Serial
    private static final long serialVersionUID = 1237027968081812208L;
    
    private final String commonName;
    private final String parentTaxonPath;
    private final String rank;
    private final Boolean isExtincted;
    private final Set<DbXrefTO> dbXrefTOs;
    
    public TaxonTO(String path, String scientificName, String commonName, String parentTaxonPath,
                   String rank, Boolean isExtincted, Collection<DbXrefTO> dbXrefTOs) {
        super(path, scientificName, null);
        this.commonName = commonName;
        this.parentTaxonPath = parentTaxonPath;
        this.rank = rank;
        this.isExtincted = isExtincted;
        this.dbXrefTOs = Collections.unmodifiableSet(dbXrefTOs == null ?
                new HashSet<>(): new HashSet<>(dbXrefTOs));
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
    
    public String getParentTaxonPath() {
        return parentTaxonPath;
    }
    
    public String getRank() {
        return rank;
    }
    
    public Boolean isExtincted() {
        return isExtincted;
    }
    
    public Set<DbXrefTO> getDbXrefTOs() {
        return dbXrefTOs;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", TaxonTO.class.getSimpleName() + "[", "]")
                .add("path=" + getPath())
                .add("scientificName='" + getScientificName() + "'")
                .add("commonName='" + commonName + "'")
                .add("parentTaxonPath='" + parentTaxonPath + "'")
                .add("rank='" + rank + "'")
                .add("isExtincted=" + isExtincted)
                .add("dbXrefTOs=" + dbXrefTOs)
                .toString();
    }
}
