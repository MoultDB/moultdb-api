package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.GeologicalAgeTO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public interface GeologicalAgeDAO extends DAO<GeologicalAgeTO> {
    
    List<GeologicalAgeTO> findAll();
    
    GeologicalAgeTO findByNotation(String id);
    
    List<GeologicalAgeTO> findByNotations(Set<String> ids);
    
    public GeologicalAgeTO findByLabelOrSynonym(String label);
    
    public List<GeologicalAgeTO> findByLabelsOrSynonyms(Set<String> labels);
    
    Integer batchUpdate(Collection<GeologicalAgeTO> geoAgeTOs);
}
