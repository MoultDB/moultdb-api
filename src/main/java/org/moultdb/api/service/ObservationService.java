package org.moultdb.api.service;

import org.moultdb.api.model.Observation;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2023-05-31
 */
public interface ObservationService {
    
    public List<Observation> getAllObservations();

    public List<Observation> getNewestObservations(int limit);

    public List<Observation> getObservationsByUsername(String username);
    
    List<Observation> getObservationsByTaxon(String taxonName);
}
