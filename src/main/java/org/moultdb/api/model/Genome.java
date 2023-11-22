package org.moultdb.api.model;

import java.time.LocalDate;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2023-11-21
 */
public class Genome extends Entity<String> {
    
    private final Taxon taxon;
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
    
    public Genome(String geneBankAcc, Taxon taxon, LocalDate submissionDate, Long length, Integer scaffolds,
                  Integer scaffoldL50, Integer scaffoldN50, LocalDate annotationDate, Integer totalGenes,
                  Float completeBusco, Float singleBusco, Float duplicatedBusco, Float fragmentedBusco, Float missingBusco) {
        super(geneBankAcc);
        this.taxon = taxon;
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
    
    public Taxon getTaxon() {
        return taxon;
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
        return new StringJoiner(", ", Genome.class.getSimpleName() + "[", "]")
                .add("geneBankAcc='" + getId() + "'")
                .add("taxon='" + taxon + "'")
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
