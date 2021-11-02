package org.moultdb.api.service;

import org.moultdb.api.model.DatedTaxon;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-26
 */
public interface DatedTaxonService extends Service {
    
    public List<DatedTaxon> getAllDatedTaxa();
    
}
