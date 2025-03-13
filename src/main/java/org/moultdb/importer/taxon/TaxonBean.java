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
    private String inatId;
    private String ncbiName;
    private String gbifName;
    private String inatName;
    private String synonymGbifIds;
    private String synonymGbifNames;
    private String synonymNcbiNames;
    
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
    
    public String getInatId() {
        return inatId;
    }
    
    public void setInatId(String inatId) {
        this.inatId = inatId;
    }
    
    public String getNcbiName() {
        return ncbiName;
    }
    
    public void setNcbiName(String ncbiName) {
        this.ncbiName = ncbiName;
    }
    
    public String getGbifName() {
        return gbifName;
    }
    
    public void setGbifName(String gbifName) {
        this.gbifName = gbifName;
    }
    
    public String getInatName() {
        return inatName;
    }
    
    public void setInatName(String inatName) {
        this.inatName = inatName;
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
    
    public String getSynonymNcbiNames() {
        return synonymNcbiNames;
    }
    
    public void setSynonymNcbiNames(String synonymNcbiNames) {
        this.synonymNcbiNames = synonymNcbiNames;
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
                && Objects.equals(inatId, taxonBean.inatId)
                && Objects.equals(ncbiName, taxonBean.ncbiName)
                && Objects.equals(gbifName, taxonBean.gbifName)
                && Objects.equals(inatName, taxonBean.inatName)
                && Objects.equals(synonymGbifIds, taxonBean.synonymGbifIds)
                && Objects.equals(synonymGbifNames, taxonBean.synonymGbifNames)
                && Objects.equals(synonymNcbiNames, taxonBean.synonymNcbiNames);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, path, ncbiId, gbifId, inatId, ncbiName, gbifName, inatName,
                synonymGbifIds, synonymGbifNames, synonymNcbiNames);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", TaxonBean.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("path='" + path + "'")
                .add("ncbiId='" + ncbiId + "'")
                .add("gbifId='" + gbifId + "'")
                .add("gbifId='" + inatId + "'")
                .add("ncbiName='" + ncbiName + "'")
                .add("gbifName='" + gbifName + "'")
                .add("gbifName='" + inatName + "'")
                .add("synonymGbifIds='" + synonymGbifIds + "'")
                .add("synonymGbifNames='" + synonymGbifNames + "'")
                .add("synonymNcbiNames='" + synonymNcbiNames + "'")
                .toString();
    }
}
