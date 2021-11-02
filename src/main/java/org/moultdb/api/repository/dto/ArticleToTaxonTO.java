package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public class ArticleToTaxonTO extends TransfertObject {
    
    @Serial
    private static final long serialVersionUID = 7259799961047341264L;
    
    private final ArticleTO articleTO;
    private final TaxonTO taxonTO;
    private final Integer versionId;
    
    public ArticleToTaxonTO(ArticleTO articleTO, TaxonTO taxonTO, Integer versionId) {
        this.articleTO = articleTO;
        this.taxonTO = taxonTO;
        this.versionId = versionId;
    }
    
    public ArticleTO getArticleTO() {
        return articleTO;
    }
    
    public TaxonTO getTaxonTO() {
        return taxonTO;
    }
    
    public Integer getVersionId() {
        return versionId;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", ArticleToTaxonTO.class.getSimpleName() + "[", "]")
                .add("articleTO=" + articleTO)
                .add("taxonTO=" + taxonTO)
                .add("versionId=" + versionId)
                .toString();
    }
}
