package org.moultdb.api.repository.dto;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-18
 */
public class TaxonDto {
    
    private final Integer id;
    private final String scientificName;
    private final String commonName;
    private final String taxonRank;
    private final Integer ncbiTaxId;
    private final boolean extinct;
    private final String path;
    
    public TaxonDto(Integer id, String scientificName, String commonName, String taxonRank, Integer ncbiTaxId, boolean extinct, String path) {
        this.id = id;
        this.scientificName = scientificName;
        this.commonName = commonName;
        this.taxonRank = taxonRank;
        this.ncbiTaxId = ncbiTaxId;
        this.extinct = extinct;
        this.path = path;
    }
    
    public int getNcbiTaxId() {
        return ncbiTaxId;
    }
    
    public String getScientificName() {
        return scientificName;
    }
    
    public String getCommonName() {
        return commonName;
    }
    
    public String getTaxonRank() {
        return taxonRank;
    }
    
    public boolean isExtinct() {
        return extinct;
    }
    
    public String getPath() {
        return path;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        
        TaxonDto taxonTO = (TaxonDto) o;
        return Objects.equals(id, taxonTO.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", TaxonDto.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("scientificName='" + scientificName + "'")
                .add("commonName='" + commonName + "'")
                .add("taxonRank='" + taxonRank + "'")
                .add("ncbiTaxId=" + ncbiTaxId)
                .add("extinct=" + extinct)
                .add("path='" + path + "'")
                .toString();
    }
}
