package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.GenomeTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2023-11-21
 */
public interface GenomeDAO extends DAO<GenomeTO> {
    
    List<GenomeTO> findAll();
    
    List<GenomeTO> findByTaxonPath(String taxonPath, boolean withSubspeciesGenomes);
    
    GenomeTO findByGenbankAcc(String genbankAcc);
    
    void insert(GenomeTO taxon);
    
    void batchUpdate(Set<GenomeTO> genomeTOs);
}
