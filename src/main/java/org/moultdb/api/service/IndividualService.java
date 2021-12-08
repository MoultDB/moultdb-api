package org.moultdb.api.service;

import org.moultdb.api.model.Individual;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-26
 */
public interface IndividualService extends Service {
    
    public List<Individual> getAllIndividuals();
    
}
