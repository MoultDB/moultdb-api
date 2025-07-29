package org.moultdb.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author  Valentine Rech de Laval
 * @since   2024-11-05
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaxonSearchResult {
    
    private final String path;
    
    private final String accession;
    
    private final String scientificName;
    
    private final String commonName;
    
    private final String synonyms;
    
    private final String xrefUrl;
    
    public TaxonSearchResult(String accession, String scientificName) {
        this(null, accession, scientificName, null, null, null);
    }
    
    public TaxonSearchResult(String path, String accession, String scientificName, String commonName, String synonyms, String xrefUrl) {
        this.path = path;
        this.accession = accession;
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.synonyms = synonyms;
        this.xrefUrl = xrefUrl;
    }
    
    public String getPath() {
        return path;
    }
    
    public String getAccession() {
        return accession;
    }
    
    public String getCommonName() {
        return commonName;
    }
    
    public String getScientificName() {
        return scientificName;
    }
    
    public String getSynonyms() {
        return synonyms;
    }
    
    public String getXrefUrl() {
        return xrefUrl;
    }
}
