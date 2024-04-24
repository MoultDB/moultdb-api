package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-01
 */
public class TaxonToDbXrefTO extends TransfertObject {
    
    @Serial
    private static final long serialVersionUID = -4406958990571834351L;
    
    private final String taxonPath;
    private final Integer dbXrefId;
    private final Boolean isMain;

    public TaxonToDbXrefTO(String taxonPath, Integer dbXrefId, Boolean isMain) {
        this.taxonPath = taxonPath;
        this.dbXrefId = dbXrefId;
        this.isMain = isMain;
    }
    
    public String getTaxonPath() {
        return taxonPath;
    }
    
    public Integer getDbXrefId() {
        return dbXrefId;
    }
    
    public Boolean getMain() {
        return isMain;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaxonToDbXrefTO that = (TaxonToDbXrefTO) o;
        return Objects.equals(taxonPath, that.taxonPath)
                && Objects.equals(dbXrefId, that.dbXrefId)
                && Objects.equals(isMain, that.isMain);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(taxonPath, dbXrefId, isMain);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", TaxonToDbXrefTO.class.getSimpleName() + "[", "]")
                .add("taxonPath='" + taxonPath + "'")
                .add("dbXrefId=" + dbXrefId)
                .add("isMain=" + isMain)
                .toString();
    }
}
