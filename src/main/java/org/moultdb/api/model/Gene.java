package org.moultdb.api.model;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-25
 */
// Gene doesn't extend NamedEntity<String> because gene id can be null but, if gene id is null, locus tag cannot be null
public class Gene {
    
    private final String id;
    private final String name;
    private final String locusTag;
    private final String genomeAcc;
    private final Taxon taxon;
    private final Integer orthogroupId;
    private final String transcriptId;
    private final String transcriptUrlSuffix;
    private final String proteinId;
    private final String proteinDescription;
    private final Integer proteinLength;
    private final DataSource dataSource;
    private final List<GeneDomain> geneDomains;
    private final Pathway pathway;
    private final String annotatedName;
    
    public Gene(String id, String name, String locusTag, Taxon taxon, String genomeAcc, Integer orthogroupId,
                String transcriptId, String transcriptUrlSuffix,
                String proteinId, String proteinDescription, Integer proteinLength,
                DataSource dataSource, Set<GeneDomain> geneDomains, Pathway pathway, String annotatedName)
            throws IllegalArgumentException {
        if (StringUtils.isBlank(id) && StringUtils.isBlank(locusTag)) {
            throw new IllegalArgumentException("The gene id and locus tag provided cannot be both blank");
        }
        this.id = id;
        this.name = name;
        this.locusTag = locusTag;
        this.genomeAcc = genomeAcc;
        this.taxon = taxon;
        this.orthogroupId = orthogroupId;
        this.transcriptId = transcriptId;
        this.transcriptUrlSuffix = transcriptUrlSuffix;
        this.proteinId = proteinId;
        this.proteinDescription = proteinDescription;
        this.proteinLength = proteinLength;
        this.dataSource = dataSource;
        this.geneDomains = Collections.unmodifiableList(geneDomains == null ?
                new ArrayList<>(): geneDomains.stream()
                .sorted(Comparator.comparing(GeneDomain::getStart).thenComparing(GeneDomain::getEnd))
                .toList());
        this.pathway = pathway;
        this.annotatedName = annotatedName;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getLocusTag() {
        return locusTag;
    }
    
    public String getGenomeAcc() {
        return genomeAcc;
    }
    
    public Taxon getTaxon() {
        return taxon;
    }
    
    public Integer getOrthogroupId() {
        return orthogroupId;
    }
    
    public String getTranscriptId() {
        return transcriptId;
    }
    
    public String getTranscriptUrlSuffix() {
        return transcriptUrlSuffix;
    }
    
    public String getProteinId() {
        return proteinId;
    }
    
    public String getProteinDescription() {
        return proteinDescription;
    }
    
    public Integer getProteinLength() {
        return proteinLength;
    }
    
    public DataSource getDataSource() {
        return dataSource;
    }
    
    public List<GeneDomain> getGeneDomains() {
        return geneDomains;
    }
    
    public Pathway getPathway() {
        return pathway;
    }
    
    public String getAnnotatedName() {
        return annotatedName;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gene gene = (Gene) o;
        return Objects.equals(id, gene.id) && Objects.equals(locusTag, gene.locusTag);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, locusTag);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Gene.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("name='" + name + "'")
                .add("locusTag='" + locusTag + "'")
                .add("genomeAcc=" + genomeAcc)
                .add("taxon=" + taxon)
                .add("orthogroupId=" + orthogroupId)
                .add("transcriptId='" + transcriptId + "'")
                .add("transcriptUrlSuffix='" + transcriptUrlSuffix + "'")
                .add("proteinId='" + proteinId + "'")
                .add("proteinDescription='" + proteinDescription + "'")
                .add("proteinLength=" + proteinLength)
                .add("dataSource=" + dataSource)
                .add("geneDomains=" + geneDomains)
                .add("pathway=" + pathway)
                .add("annotatedName=" + annotatedName)
                .toString();
    }
    
    public static class GeneDomain {
        
        private final Domain domain;
        private final Integer start;
        private final Integer end;
        
        public GeneDomain(Domain domain, Integer start, Integer end) {
            this.domain = domain;
            this.start = start;
            this.end = end;
        }
        
        public Domain getDomain() {
            return domain;
        }
        
        public Integer getStart() {
            return start;
        }
        
        public Integer getEnd() {
            return end;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GeneDomain that = (GeneDomain) o;
            return Objects.equals(domain, that.domain) && Objects.equals(start, that.start) && Objects.equals(end, that.end);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(domain, start, end);
        }
        
        @Override
        public String toString() {
            return new StringJoiner(", ", GeneDomain.class.getSimpleName() + "[", "]")
                    .add("domain=" + domain)
                    .add("start=" + start)
                    .add("end=" + end)
                    .toString();
        }
    }
}
