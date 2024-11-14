package org.moultdb.api.repository.dao;

import org.moultdb.api.model.ReleaseVersion;

/**
 * @author Valentine Rech de Laval
 * @since 2024-11-14
 */
public interface ReleaseDAO {

    int findAnnotationCount();
    
    int findGenomeCount();

    ReleaseVersion findReleaseVersion();

    int insertNewReleaseVersion();
}
