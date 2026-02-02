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
    private final String rank;
    private final Set<DbXrefTO> dbXrefTOs;
    private final Set<TaxonToDbXrefTO> taxonToDbXrefTOs;
    
    public TaxonTO(String path, String scientificName, String commonName,
                   String rank, Collection<DbXrefTO> dbXrefTOs) {
        this(path, scientificName, commonName, rank, dbXrefTOs, null);
    }
    
    public TaxonTO(String path, String scientificName, String commonName,
                   String rank, Collection<DbXrefTO> dbXrefTOs,
                   Collection<TaxonToDbXrefTO> taxonToDbXrefTOs) {
        super(path, scientificName, null);
        this.commonName = commonName;
        this.rank = rank;
        this.dbXrefTOs = Collections.unmodifiableSet(dbXrefTOs == null ?
                new HashSet<>(): new HashSet<>(dbXrefTOs));
        this.taxonToDbXrefTOs = Collections.unmodifiableSet(taxonToDbXrefTOs == null ?
                new HashSet<>(): new HashSet<>(taxonToDbXrefTOs));
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
    
    public Set<DbXrefTO> getDbXrefTOs() {
        return dbXrefTOs;
    }
    
    public Set<TaxonToDbXrefTO> getTaxonToDbXrefTOs() {
        return taxonToDbXrefTOs;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", TaxonTO.class.getSimpleName() + "[", "]")
                .add("path=" + getPath())
                .add("scientificName='" + getScientificName() + "'")
                .add("commonName='" + commonName + "'")
                .add("rank=" + rank)
                .add("dbXrefTOs=" + dbXrefTOs)
                .add("taxonToDbXrefTOs=" + taxonToDbXrefTOs)
                .toString();
    }
}
