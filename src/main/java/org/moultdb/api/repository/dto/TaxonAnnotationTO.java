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
    private final String authorSpeciesName;
    private final Integer moultingCharactersId;
    private final Integer sampleSetId;
    private final String specimenCount;
    private final ConditionTO conditionTO;
    private final String authorDevStage;
    private final String authorAnatEntity;
    private final ArticleTO articleTO;
    private final ObservationTO observationTO;
    private final ECOTermTO ecoTermTO;
    private final CIOStatementTO cioStatementTO;
    private final String determinedBy;
    private final Integer versionId;
    
    public TaxonAnnotationTO(Integer id, TaxonTO taxonTO, String authorSpeciesName, String determinedBy,
                             Integer sampleSetId, String specimenCount, ConditionTO conditionTO, String authorDevStage,
                             String authorAnatEntity, ArticleTO articleTO, ObservationTO observationTO, Integer moultingCharactersId,
                             ECOTermTO ecoTermTO, CIOStatementTO cioStatementTO, Integer versionId) {
        super(id);
        this.taxonTO = taxonTO;
        this.authorSpeciesName = authorSpeciesName;
        this.authorDevStage = authorDevStage;
        this.authorAnatEntity = authorAnatEntity;
        this.moultingCharactersId = moultingCharactersId;
        this.sampleSetId = sampleSetId;
        this.specimenCount = specimenCount;
        this.conditionTO = conditionTO;
        this.articleTO = articleTO;
        this.observationTO = observationTO;
        this.ecoTermTO = ecoTermTO;
        this.cioStatementTO = cioStatementTO;
        this.determinedBy = determinedBy;
        this.versionId = versionId;
    }
    
    public TaxonTO getTaxonTO() {
        return taxonTO;
    }
    
    public String getAuthorSpeciesName() {
        return authorSpeciesName;
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
    
    public String getAuthorDevStage() {
        return authorDevStage;
    }
    
    public String getAuthorAnatEntity() {
        return authorAnatEntity;
    }
    
    public ArticleTO getArticleTO() {
        return articleTO;
    }
    
    public ObservationTO getObservationTO() {
        return observationTO;
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
                .add("authorSpeciesName='" + authorSpeciesName + "'")
                .add("moultingCharactersId=" + moultingCharactersId)
                .add("sampleSetId=" + sampleSetId)
                .add("specimenCount='" + specimenCount + "'")
                .add("conditionTO=" + conditionTO)
                .add("authorDevStage='" + authorDevStage + "'")
                .add("authorAnatEntity='" + authorAnatEntity + "'")
                .add("articleTO=" + articleTO)
                .add("observationTO=" + observationTO)
                .add("ecoTermTO=" + ecoTermTO)
                .add("cioStatementTO=" + cioStatementTO)
                .add("determinedBy='" + determinedBy + "'")
                .add("versionId=" + versionId)
                .toString();
    }
}
