package org.moultdb.api.model;

import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2024-11-14
 */
public record Statistics(Integer genomeCount, Integer annotationCount) {
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Statistics.class.getSimpleName() + "[", "]")
                .add("genomeCount=" + genomeCount)
                .add("annotationCount=" + annotationCount)
                .toString();
    }
}
