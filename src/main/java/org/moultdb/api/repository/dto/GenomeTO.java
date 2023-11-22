package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.time.LocalDate;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2023-11-21
 */
public class GenomeTO extends EntityTO<String> {
    
    @Serial
    private static final long serialVersionUID = 8611347002947346007L;
    
    private final TaxonTO taxonTO;
    private final LocalDate submissionDate;
    private final Long length;
    private final Integer scaffolds;
    private final Integer scaffoldL50;
    private final Integer scaffoldN50;
    private final LocalDate annotationDate;
    private final Integer totalGenes;
    private final Float completeBusco;
    private final Float singleBusco;
    private final Float duplicatedBusco;
    private final Float fragmentedBusco;
    private final Float missingBusco;
    
    public GenomeTO(String geneBankAcc, TaxonTO taxonTO, LocalDate submissionDate, Long length, Integer scaffolds,
                    Integer scaffoldL50, Integer scaffoldN50, LocalDate annotationDate, Integer totalGenes,
                    Float completeBusco, Float singleBusco, Float duplicatedBusco, Float fragmentedBusco, Float missingBusco) {
        super(geneBankAcc);
        this.taxonTO = taxonTO;
        this.submissionDate = submissionDate;
        this.length = length;
        this.scaffolds = scaffolds;
        this.scaffoldL50 = scaffoldL50;
        this.scaffoldN50 = scaffoldN50;
        this.annotationDate = annotationDate;
        this.totalGenes = totalGenes;
        this.completeBusco = completeBusco;
        this.singleBusco = singleBusco;
        this.duplicatedBusco = duplicatedBusco;
        this.fragmentedBusco = fragmentedBusco;
        this.missingBusco = missingBusco;
    }
    
    public String getGeneBankAcc() {
        return getId();
    }
    
    public TaxonTO getTaxonTO() {
        return taxonTO;
    }
    
    public LocalDate getSubmissionDate() {
        return submissionDate;
    }
    
    public Long getLength() {
        return length;
    }
    
    public Integer getScaffolds() {
        return scaffolds;
    }
    
    public Integer getScaffoldL50() {
        return scaffoldL50;
    }
    
    public Integer getScaffoldN50() {
        return scaffoldN50;
    }
    
    public LocalDate getAnnotationDate() {
        return annotationDate;
    }
    
    public Integer getTotalGenes() {
        return totalGenes;
    }
    
    public Float getCompleteBusco() {
        return completeBusco;
    }
    
    public Float getSingleBusco() {
        return singleBusco;
    }
    
    public Float getDuplicatedBusco() {
        return duplicatedBusco;
    }
    
    public Float getFragmentedBusco() {
        return fragmentedBusco;
    }
    
    public Float getMissingBusco() {
        return missingBusco;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", GenomeTO.class.getSimpleName() + "[", "]")
                .add("geneBankAcc='" + getGeneBankAcc() + "'")
                .add("taxonTO='" + taxonTO + "'")
                .add("submissionDate=" + submissionDate)
                .add("length=" + length)
                .add("scaffolds=" + scaffolds)
                .add("scaffoldL50=" + scaffoldL50)
                .add("scaffoldN50=" + scaffoldN50)
                .add("annotationDate=" + annotationDate)
                .add("totalGenes=" + totalGenes)
                .add("completeBusco=" + completeBusco)
                .add("singleBusco=" + singleBusco)
                .add("duplicatedBusco=" + duplicatedBusco)
                .add("fragmentedBusco=" + fragmentedBusco)
                .add("missingBusco=" + missingBusco)
                .toString();
    }
}
