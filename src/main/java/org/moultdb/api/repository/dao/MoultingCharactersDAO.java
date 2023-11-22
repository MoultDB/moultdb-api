package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.MoultingCharactersTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public interface MoultingCharactersDAO extends DAO<MoultingCharactersTO> {
    
    List<MoultingCharactersTO> findAll();
    
    MoultingCharactersTO findById(int id);
    
    List<MoultingCharactersTO> findByIds(Set<Integer> id);
    
    Integer getLastId();
    
    void insert(MoultingCharactersTO moultingCharactersTO);
    
    void batchUpdate(Set<MoultingCharactersTO> moultingCharactersTOs);
}
