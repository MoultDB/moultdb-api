package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.GeneralMoultingCharactersTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public interface GeneralMoultingCharactersDAO extends DAO<GeneralMoultingCharactersTO> {
    
    List<GeneralMoultingCharactersTO> findAll();
    
    GeneralMoultingCharactersTO findById(int id);
    
    List<GeneralMoultingCharactersTO> findByIds(Set<Integer> id);
    
}
