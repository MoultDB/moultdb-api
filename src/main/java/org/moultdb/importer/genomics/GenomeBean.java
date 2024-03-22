package org.moultdb.importer.genomics;

import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2023-11-21
 */
public class GenomeBean {
    
    private String genbankAcc;
    private String taxonId;
    private Date submissionDate;
    private String subphylum;
    private String order;
    private String genus;
    private String species;
    private Long length;
    private Integer scaffolds;
    private Integer scaffoldL50;
    private Integer scaffoldN50;
    private Date annotationDate;
    private Integer totalGenes;
    private Double completeBusco;
    private Double singleBusco;
    private Double duplicatedBusco;
    private Double fragmentedBusco;
    private Double missingBusco;
    
    public GenomeBean() {
    }
    
    public String getGenbankAcc() {
        return genbankAcc;
    }
    
    public void setGenbankAcc(String genbankAcc) {
        this.genbankAcc = genbankAcc;
    }
    
    public String getTaxonId() {
        return taxonId;
    }
    
    public void setTaxonId(String taxonId) {
        this.taxonId = taxonId;
    }
    
    public Date getSubmissionDate() {
        return submissionDate;
    }
    
    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }
    
    public String getSubphylum() {
        return subphylum;
    }
    
    public void setSubphylum(String subphylum) {
        this.subphylum = subphylum;
    }
    
    public String getOrder() {
        return order;
    }
    
    public void setOrder(String order) {
        this.order = order;
    }
    
    public String getGenus() {
        return genus;
    }
    
    public void setGenus(String genus) {
        this.genus = genus;
    }
    
    public String getSpecies() {
        return species;
    }
    
    public void setSpecies(String species) {
        this.species = species;
    }
    
    public Long getLength() {
        return length;
    }
    
    public void setLength(Long length) {
        this.length = length;
    }
    
    public Integer getScaffolds() {
        return scaffolds;
    }
    
    public void setScaffolds(Integer scaffolds) {
        this.scaffolds = scaffolds;
    }
    
    public Integer getScaffoldL50() {
        return scaffoldL50;
    }
    
    public void setScaffoldL50(Integer scaffoldL50) {
        this.scaffoldL50 = scaffoldL50;
    }
    
    public Integer getScaffoldN50() {
        return scaffoldN50;
    }
    
    public void setScaffoldN50(Integer scaffoldN50) {
        this.scaffoldN50 = scaffoldN50;
    }
    
    public Date getAnnotationDate() {
        return annotationDate;
    }
    
    public void setAnnotationDate(Date annotationDate) {
        this.annotationDate = annotationDate;
    }
    
    public Integer getTotalGenes() {
        return totalGenes;
    }
    
    public void setTotalGenes(Integer totalGenes) {
        this.totalGenes = totalGenes;
    }
    
    public Double getCompleteBusco() {
        return completeBusco;
    }
    
    public void setCompleteBusco(Double completeBusco) {
        this.completeBusco = completeBusco;
    }
    
    public Double getSingleBusco() {
        return singleBusco;
    }
    
    public void setSingleBusco(Double singleBusco) {
        this.singleBusco = singleBusco;
    }
    
    public Double getDuplicatedBusco() {
        return duplicatedBusco;
    }
    
    public void setDuplicatedBusco(Double duplicatedBusco) {
        this.duplicatedBusco = duplicatedBusco;
    }
    
    public Double getFragmentedBusco() {
        return fragmentedBusco;
    }
    
    public void setFragmentedBusco(Double fragmentedBusco) {
        this.fragmentedBusco = fragmentedBusco;
    }
    
    public Double getMissingBusco() {
        return missingBusco;
    }
    
    public void setMissingBusco(Double missingBusco) {
        this.missingBusco = missingBusco;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenomeBean that = (GenomeBean) o;
        return Objects.equals(genbankAcc, that.genbankAcc)
                && Objects.equals(taxonId, that.taxonId)
                && Objects.equals(submissionDate, that.submissionDate)
                && Objects.equals(subphylum, that.subphylum)
                && Objects.equals(order, that.order)
                && Objects.equals(genus, that.genus)
                && Objects.equals(species, that.species)
                && Objects.equals(length, that.length)
                && Objects.equals(scaffolds, that.scaffolds)
                && Objects.equals(scaffoldL50, that.scaffoldL50)
                && Objects.equals(scaffoldN50, that.scaffoldN50)
                && Objects.equals(annotationDate, that.annotationDate)
                && Objects.equals(totalGenes, that.totalGenes)
                && Objects.equals(completeBusco, that.completeBusco)
                && Objects.equals(singleBusco, that.singleBusco)
                && Objects.equals(duplicatedBusco, that.duplicatedBusco)
                && Objects.equals(fragmentedBusco, that.fragmentedBusco)
                && Objects.equals(missingBusco, that.missingBusco);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(genbankAcc, taxonId, submissionDate, subphylum, order, genus, species, length, scaffolds,
                scaffoldL50, scaffoldN50, annotationDate, totalGenes, completeBusco, singleBusco, duplicatedBusco,
                fragmentedBusco, missingBusco);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", GenomeBean.class.getSimpleName() + "[", "]")
                .add("genBankAcc='" + genbankAcc + "'")
                .add("taxonId='" + taxonId + "'")
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
