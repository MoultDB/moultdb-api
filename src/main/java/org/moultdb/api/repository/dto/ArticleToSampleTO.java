//package org.moultdb.api.repository.dto;
//
//import java.io.Serial;
//import java.util.StringJoiner;
//
///**
// * @author Valentine Rech de Laval
// * @since 2021-10-25
// */
//public class ArticleToSampleTO extends TransfertObject {
//
//    @Serial
//    private static final long serialVersionUID = 7259799961047341264L;
//
//    private final Integer articleId;
//    private final Integer sampleId;
//
//    public ArticleToSampleTO(Integer articleId, Integer sampleId) {
//        this.articleId = articleId;
//        this.sampleId = sampleId;
//    }
//
//    public Integer getArticleId() {
//        return articleId;
//    }
//
//    public Integer getSampleId() {
//        return sampleId;
//    }
//
//    @Override
//    public String toString() {
//        return new StringJoiner(", ", ArticleToSampleTO.class.getSimpleName() + "[", "]")
//                .add("articleId=" + articleId)
//                .add("sampleId=" + sampleId)
//                .toString();
//    }
//}
