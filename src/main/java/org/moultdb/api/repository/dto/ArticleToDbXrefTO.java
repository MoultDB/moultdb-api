//package org.moultdb.api.repository.dto;
//
//import java.io.Serial;
//import java.util.StringJoiner;
//
///**
// * @author Valentine Rech de Laval
// * @since 2021-11-01
// */
//public class ArticleToDbXrefTO extends TransfertObject {
//
//    @Serial
//    private static final long serialVersionUID = -993911556511063937L;
//
//    private final Integer articleId;
//    private final Integer dbXrefId;
//
//    public ArticleToDbXrefTO(Integer articleId, Integer dbXrefId) {
//        this.articleId = articleId;
//        this.dbXrefId = dbXrefId;
//    }
//
//    public Integer getArticleId() {
//        return articleId;
//    }
//
//    public Integer getDbXrefId() {
//        return dbXrefId;
//    }
//
//    @Override
//    public String toString() {
//        return new StringJoiner(", ", ArticleToDbXrefTO.class.getSimpleName() + "[", "]")
//                .add("articleId=" + articleId)
//                .add("dbXrefId=" + dbXrefId)
//                .toString();
//    }
//}
