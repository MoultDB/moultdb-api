package org.moultdb.api.model;

import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2024-11-14
 */
public record ReleaseInfo (ReleaseVersion releaseVersion, Statistics statistics) {
    @Override
    public String toString() {
        return new StringJoiner(", ", ReleaseInfo.class.getSimpleName() + "[", "]")
                .add("releaseVersion=" + releaseVersion)
                .add("statisticsResult=" + statistics)
                .toString();
    }
}
