package org.moultdb.api.service;

import org.moultdb.api.model.GeologicalAge;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2022-05-10
 */
public interface GeologicalAgeService {
    
    public List<GeologicalAge> getAllGeologicalAges();
    
    public Integer updateGeologicalAges(String pwd);
}
