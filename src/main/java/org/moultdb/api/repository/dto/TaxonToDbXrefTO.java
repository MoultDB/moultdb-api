package org.moultdb.api.repository.dto;

import java.io.Serial;
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
    public String toString() {
        return new StringJoiner(", ", TaxonToDbXrefTO.class.getSimpleName() + "[", "]")
                .add("taxonPath='" + taxonPath + "'")
                .add("dbXrefId=" + dbXrefId)
                .add("isMain=" + isMain)
                .toString();
    }
}
