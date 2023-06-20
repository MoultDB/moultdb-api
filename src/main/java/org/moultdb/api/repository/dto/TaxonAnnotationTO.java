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
    private final ConditionTO conditionTO;
    private final ArticleTO articleTO;
    private final ImageTO imageTO;
    private final TermTO ecoTO;
    private final TermTO cioTO;
    private final Integer versionId;
    
    public TaxonAnnotationTO(Integer id, TaxonTO taxonTO, String annotatedSpeciesName,
                             Integer sampleSetId, ConditionTO conditionTO, ImageTO imageTO,
                             Integer versionId) {
        this(id, taxonTO, annotatedSpeciesName, null, sampleSetId, conditionTO, null,
                imageTO, null, null, versionId);
    }
    
    public TaxonAnnotationTO(Integer id, TaxonTO taxonTO, String annotatedSpeciesName,
                             Integer moultingCharactersId, Integer sampleSetId,
                             ConditionTO conditionTO, ArticleTO articleTO, ImageTO imageTO,
                             TermTO ecoTO, TermTO cioTO, Integer versionId) {
        super(id);
        this.taxonTO = taxonTO;
        this.annotatedSpeciesName = annotatedSpeciesName;
        this.moultingCharactersId = moultingCharactersId;
        this.sampleSetId = sampleSetId;
        this.conditionTO = conditionTO;
        this.articleTO = articleTO;
        this.imageTO = imageTO;
        this.ecoTO = ecoTO;
        this.cioTO = cioTO;
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
    
    public ConditionTO getConditionTO() {
        return conditionTO;
    }
    
    public ArticleTO getArticleTO() {
        return articleTO;
    }
    
    public ImageTO getImageTO() {
        return imageTO;
    }
    
    public TermTO getEcoTO() {
        return ecoTO;
    }
    
    public TermTO getCioTO() {
        return cioTO;
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
                .add("conditionTO=" + conditionTO)
                .add("articleTO=" + articleTO)
                .add("imageTO=" + imageTO)
                .add("ecoTO=" + ecoTO)
                .add("cioTO=" + cioTO)
                .add("versionId=" + versionId)
                .toString();
    }
}
