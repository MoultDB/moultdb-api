//package org.moultdb.api.repository.dto;
//
//import java.io.Serial;
//import java.util.StringJoiner;
//
///**
// * @author Valentine Rech de Laval
// * @since 2021-10-25
// */
//public class ArticleToTaxonTO extends TransfertObject {
//
//    @Serial
//    private static final long serialVersionUID = 7259799961047341264L;
//
//    private final Integer articleId;
//    private final Integer taxonId;
//    private final Integer versionId;
//
//    public ArticleToTaxonTO(Integer articleId, Integer taxonId, Integer versionId) {
//        this.articleId = articleId;
//        this.taxonId = taxonId;
//        this.versionId = versionId;
//    }
//
//    public Integer getArticleId() {
//        return articleId;
//    }
//
//    public Integer getTaxonId() {
//        return taxonId;
//    }
//
//    public Integer getVersionId() {
//        return versionId;
//    }
//
//    @Override
//    public String toString() {
//        return new StringJoiner(", ", ArticleToTaxonTO.class.getSimpleName() + "[", "]")
//                .add("articleId=" + articleId)
//                .add("taxonId=" + taxonId)
//                .add("versionId=" + versionId)
//                .toString();
//    }
//}
