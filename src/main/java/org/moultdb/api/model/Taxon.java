package org.moultdb.api.model;

import java.util.Objects;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-14
 */
public class Taxon {
    
    private final String scientificName;
    private final String commonName;
    private final String taxonRank;
    private final Integer ncbiTaxId;
    private final boolean extinct;
    private final String path;
    
    public Taxon(String scientificName, String commonName, String taxonRank, Integer ncbiTaxId, boolean extinct, String path) {
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
        Taxon taxon = (Taxon) o;
        return Objects.equals(scientificName, taxon.scientificName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(scientificName);
    }
    
    @Override
    public String toString() {
        return "Taxon{scientificName='" + scientificName + "', commonName='" + commonName + "', " +
                "taxonRank='" + taxonRank + "', ncbiTaxId=" + ncbiTaxId + ", extinct=" + extinct + ", " +
                "path='" + path + "'}'";
    }
}
