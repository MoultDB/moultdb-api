package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-01
 */
public class ArticleToDbXrefTO extends TransfertObject {
    
    @Serial
    private static final long serialVersionUID = -993911556511063937L;
    
    private final ArticleTO articleTO;
    private final DbXrefTO dbXrefTO;
    
    public ArticleToDbXrefTO(ArticleTO articleTO, DbXrefTO dbXrefTO) {
        this.articleTO = articleTO;
        this.dbXrefTO = dbXrefTO;
    }
    
    public ArticleTO getArticleTO() {
        return articleTO;
    }
    
    public DbXrefTO getDbXrefTO() {
        return dbXrefTO;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", ArticleToDbXrefTO.class.getSimpleName() + "[", "]")
                .add("articleTO=" + articleTO)
                .add("dbXrefTO=" + dbXrefTO)
                .toString();
    }
}
