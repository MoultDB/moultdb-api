package org.moultdb.importer.taxon;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-12-13
 */
public class TaxonBean {
    
    private Integer id;
    private String path;
    private String ncbiId;
    private String gbifId;
    private String scientificName;
    private String ncbiRank;
    private String gbifRank;
    private String synonymGbifIds;
    private String synonymGbifNames;
    
    public TaxonBean() {
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public String getNcbiId() {
        return ncbiId;
    }
    
    public void setNcbiId(String ncbiId) {
        this.ncbiId = ncbiId;
    }
    
    public String getGbifId() {
        return gbifId;
    }
    
    public void setGbifId(String gbifId) {
        this.gbifId = gbifId;
    }
    
    public String getScientificName() {
        return scientificName;
    }
    
    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }
    
    public String getNcbiRank() {
        return ncbiRank;
    }
    
    public void setNcbiRank(String ncbiRank) {
        this.ncbiRank = ncbiRank;
    }
    
    public String getGbifRank() {
        return gbifRank;
    }
    
    public void setGbifRank(String gbifRank) {
        this.gbifRank = gbifRank;
    }
    
    public String getSynonymGbifIds() {
        return synonymGbifIds;
    }
    
    public void setSynonymGbifIds(String synonymGbifIds) {
        this.synonymGbifIds = synonymGbifIds;
    }
    
    public String getSynonymGbifNames() {
        return synonymGbifNames;
    }
    
    public void setSynonymGbifNames(String synonymGbifNames) {
        this.synonymGbifNames = synonymGbifNames;
    }
    
    private String convertToLowerCase(String s) {
        return StringUtils.isBlank(s) ? null : s.toLowerCase();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaxonBean taxonBean = (TaxonBean) o;
        return Objects.equals(id, taxonBean.id)
                && Objects.equals(path, taxonBean.path)
                && Objects.equals(ncbiId, taxonBean.ncbiId)
                && Objects.equals(gbifId, taxonBean.gbifId)
                && Objects.equals(scientificName, taxonBean.scientificName)
                && Objects.equals(ncbiRank, taxonBean.ncbiRank)
                && Objects.equals(gbifRank, taxonBean.gbifRank)
                && Objects.equals(synonymGbifIds, taxonBean.synonymGbifIds)
                && Objects.equals(synonymGbifNames, taxonBean.synonymGbifNames);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, path, ncbiId, gbifId, scientificName, ncbiRank, gbifRank,
                synonymGbifIds, synonymGbifNames);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", TaxonBean.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("path='" + path + "'")
                .add("ncbiId='" + ncbiId + "'")
                .add("gbifId='" + gbifId + "'")
                .add("scientificName='" + scientificName + "'")
                .add("ncbiRank='" + ncbiRank + "'")
                .add("gbifRank='" + gbifRank + "'")
                .add("synonymGbifIds='" + synonymGbifIds + "'")
                .add("synonymGbifNames='" + synonymGbifNames + "'")
                .toString();
    }
}
