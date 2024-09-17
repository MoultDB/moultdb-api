package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-22
 */
public class TaxonAnnotationTO extends EntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = 5144072593844456844L;
    
    private final TaxonTO taxonTO;
    private final String annotatedSpeciesName;
    private final Integer moultingCharactersId;
    private final Integer sampleSetId;
    private final String specimenCount;
    private final ConditionTO conditionTO;
    private final String annotatedDevStage;
    private final String annotatedAnatEntity;
    private final ArticleTO articleTO;
    private final ImageTO imageTO;
    private final ECOTermTO ecoTermTO;
    private final CIOStatementTO cioStatementTO;
    private final String determinedBy;
    private final Integer versionId;
    
    public TaxonAnnotationTO(Integer id, TaxonTO taxonTO, String annotatedSpeciesName,
                             Integer sampleSetId, String specimenCount, ConditionTO conditionTO, ImageTO imageTO,
                             String determinedBy, Integer versionId, String annotatedDevStage, String annotatedAnatEntity) {
        this(id, taxonTO, annotatedSpeciesName, determinedBy, sampleSetId, specimenCount, conditionTO, annotatedAnatEntity, annotatedDevStage,
                null, imageTO, null, null, null, versionId);
    }
    
    public TaxonAnnotationTO(Integer id, TaxonTO taxonTO, String annotatedSpeciesName, String determinedBy,
                             Integer sampleSetId, String specimenCount, ConditionTO conditionTO, String annotatedDevStage,
                             String annotatedAnatEntity, ArticleTO articleTO, ImageTO imageTO, Integer moultingCharactersId,
                             ECOTermTO ecoTermTO, CIOStatementTO cioStatementTO, Integer versionId) {
        super(id);
        this.taxonTO = taxonTO;
        this.annotatedSpeciesName = annotatedSpeciesName;
        this.annotatedDevStage = annotatedDevStage;
        this.annotatedAnatEntity = annotatedAnatEntity;
        this.moultingCharactersId = moultingCharactersId;
        this.sampleSetId = sampleSetId;
        this.specimenCount = specimenCount;
        this.conditionTO = conditionTO;
        this.articleTO = articleTO;
        this.imageTO = imageTO;
        this.ecoTermTO = ecoTermTO;
        this.cioStatementTO = cioStatementTO;
        this.determinedBy = determinedBy;
        this.versionId = versionId;
    }
    
    public TaxonTO getTaxonTO() {
        return taxonTO;
    }
    
    public String getAnnotatedSpeciesName() {
        return annotatedSpeciesName;
    }
    
    public Integer getMoultingCharactersId() {
        return moultingCharactersId;
    }
    
    public Integer getSampleSetId() {
        return sampleSetId;
    }
    
    public String getSpecimenCount() {
        return specimenCount;
    }
    
    public ConditionTO getConditionTO() {
        return conditionTO;
    }
    
    public String getAnnotatedDevStage() {
        return annotatedDevStage;
    }
    
    public String getAnnotatedAnatEntity() {
        return annotatedAnatEntity;
    }
    
    public ArticleTO getArticleTO() {
        return articleTO;
    }
    
    public ImageTO getImageTO() {
        return imageTO;
    }
    
    public ECOTermTO getEcoTermTO() {
        return ecoTermTO;
    }
    
    public CIOStatementTO getCioStatementTO() {
        return cioStatementTO;
    }
    
    public String getDeterminedBy() {
        return determinedBy;
    }
    
    public Integer getVersionId() {
        return versionId;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", TaxonAnnotationTO.class.getSimpleName() + "[", "]")
                .add("id=" + getId())
                .add("taxonTO=" + taxonTO)
                .add("annotatedSpeciesName=" + annotatedSpeciesName)
                .add("moultingCharactersId=" + moultingCharactersId)
                .add("sampleId=" + sampleSetId)
                .add("specimenCount=" + specimenCount)
                .add("conditionTO=" + conditionTO)
                .add("articleTO=" + articleTO)
                .add("imageTO=" + imageTO)
                .add("ecoTermTO=" + ecoTermTO)
                .add("cioStatementTO=" + cioStatementTO)
                .add("determinedBy=" + determinedBy)
                .add("versionId=" + versionId)
                .toString();
    }
}
