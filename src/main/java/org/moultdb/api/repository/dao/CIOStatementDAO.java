package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.CIOStatementTO;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2024-09-17
 */
public interface CIOStatementDAO extends DAO<CIOStatementTO> {
    
    List<CIOStatementTO> findAll();
    
    CIOStatementTO findById(String id);
}