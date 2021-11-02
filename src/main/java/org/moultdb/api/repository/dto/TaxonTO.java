package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-18
 */
public class TaxonTO extends NamedEntityTO {
    
    @Serial
    private static final long serialVersionUID = 1237027968081812208L;
    
    private final String commonName;
    private final Integer dbXrefId;
    private final Integer parentTaxonId;
    private final String taxonRank;
    private final Boolean extinct;
    private final String path;
    
    public TaxonTO(Integer id, String scientificName, String commonName, Integer dbXrefId, Integer parentTaxonId,
                   String taxonRank, Boolean extinct, String path) {
        super(id, scientificName);
        this.commonName = commonName;
        this.dbXrefId = dbXrefId;
        this.parentTaxonId = parentTaxonId;
        this.taxonRank = taxonRank;
        this.extinct = extinct;
        this.path = path;
    }
    
    public String getCommonName() {
        return commonName;
    }
    
    public Integer getDbXrefId() {
        return dbXrefId;
    }
    
    public Integer getParentTaxonId() {
        return parentTaxonId;
    }
    
    public String getTaxonRank() {
        return taxonRank;
    }
    
    public Boolean isExtinct() {
        return extinct;
    }
    
    public String getPath() {
        return path;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", TaxonTO.class.getSimpleName() + "[", "]")
                .add("id=" + getId())
                .add("scientificName='" + getName() + "'")
                .add("commonName='" + commonName + "'")
                .add("dbXrefId='" + dbXrefId + "'")
                .add("parentTaxonId='" + parentTaxonId + "'")
                .add("taxonRank='" + taxonRank + "'")
                .add("extinct=" + extinct)
                .add("path='" + path + "'")
                .toString();
    }
}
