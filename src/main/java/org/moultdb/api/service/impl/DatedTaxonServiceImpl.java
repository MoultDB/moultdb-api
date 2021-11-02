package org.moultdb.api.service.impl;

import org.moultdb.api.model.DatedTaxon;
import org.moultdb.api.repository.dao.DatedTaxonDAO;
import org.moultdb.api.repository.dao.DbXrefDAO;
import org.moultdb.api.repository.dao.GeneralMoultingCharactersDAO;
import org.moultdb.api.repository.dao.TaxonDAO;
import org.moultdb.api.repository.dao.VersionDAO;
import org.moultdb.api.repository.dto.DatedTaxonTO;
import org.moultdb.api.repository.dto.DbXrefTO;
import org.moultdb.api.repository.dto.EntityTO;
import org.moultdb.api.repository.dto.GeneralMoultingCharactersTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.api.repository.dto.VersionTO;
import org.moultdb.api.service.DatedTaxonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-26
 */
@Service
public class DatedTaxonServiceImpl implements DatedTaxonService {
    
    @Autowired
    private DatedTaxonDAO datedTaxonDao;
    
    @Autowired
    private DbXrefDAO dbXrefDAO;
    
    @Autowired
    private GeneralMoultingCharactersDAO generalMoultingCharactersDAO;
    
    @Autowired
    private TaxonDAO taxonDAO;
    
    @Autowired
    private VersionDAO versionDAO;
    
    @Override
    public List<DatedTaxon> getAllDatedTaxa() {
        List<DatedTaxonTO> allDatedTaxonTOs = datedTaxonDao.findAll();
        
        Set<Integer> datedTaxonIds = allDatedTaxonTOs.parallelStream()
                                               .map(EntityTO::getId)
                                               .collect(Collectors.toSet());
        
        Map<Integer, GeneralMoultingCharactersTO> charactersTOsByDatedTaxonId =
                generalMoultingCharactersDAO.findByIds(datedTaxonIds).stream()
                                            .collect(Collectors.toMap(GeneralMoultingCharactersTO::getDatedTaxonId, Function.identity()));
    
        Set<Integer> versionIds = allDatedTaxonTOs.parallelStream()
                                                  .map(DatedTaxonTO::getVersionId)
                                                  .collect(Collectors.toSet());
        versionIds.addAll(charactersTOsByDatedTaxonId.values().parallelStream()
                                          .map(GeneralMoultingCharactersTO::getVersionId)
                                          .collect(Collectors.toSet()));
        Map<Integer, VersionTO> versionTOsById = versionDAO.findByIds(versionIds).stream()
                                                           .collect(Collectors.toMap(VersionTO::getId, Function.identity()));
    
        Set<Integer> taxonIds = allDatedTaxonTOs.parallelStream()
                                                  .map(DatedTaxonTO::getTaxonId)
                                                  .collect(Collectors.toSet());
        Map<Integer, TaxonTO> taxonTOsById = taxonDAO.findByIds(taxonIds).stream()
                                                     .collect(Collectors.toMap(TaxonTO::getId, Function.identity()));
    
        Set<Integer> dbXrefIds = taxonTOsById.values().parallelStream()
                                                .map(TaxonTO::getDbXrefId)
                                                .collect(Collectors.toSet());
    
        Map<Integer, DbXrefTO> dbXrefTOsById = dbXrefDAO.findByIds(dbXrefIds).stream()
                                                        .collect(Collectors.toMap(DbXrefTO::getId, Function.identity()));
        return allDatedTaxonTOs.stream()
                       .map(datedTaxonTO -> org.moultdb.api.service.Service.mapFromTO(
                               datedTaxonTO, charactersTOsByDatedTaxonId, versionTOsById, taxonTOsById, dbXrefTOsById))
                       .collect(Collectors.toList());
    }
}
