package org.moultdb.api.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.model.GeologicalAge;
import org.moultdb.api.repository.dao.GeologicalAgeDAO;
import org.moultdb.api.repository.dto.GeologicalAgeTO;
import org.moultdb.api.service.GeologicalAgeService;
import org.moultdb.api.service.ServiceUtils;
import org.moultdb.importer.geologicalage.GeologicalAgeImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2022-05-10
 */
@Service
public class GeologicalAgeServiceImpl implements GeologicalAgeService {
    
    private final static Logger logger = LogManager.getLogger(GeologicalAgeServiceImpl.class.getName());
    
    @Autowired
    private GeologicalAgeDAO geologicalAgeDAO;
    
    @Override
    public List<GeologicalAge> getAllGeologicalAges() {
        return getGeologicalAges(geologicalAgeDAO.findAll());
    }
    
    @Override
    public void updateGeologicalAges() {
        logger.info("Start geological ages import...");
    
        GeologicalAgeImporter importer = new GeologicalAgeImporter();
        Set<GeologicalAgeTO> geologicalAgeTOs = importer.getGeologicalAgeTOs();
    
        geologicalAgeDAO.batchUpdate(geologicalAgeTOs);
    
        logger.info("End geological ages import.");
    }
    
    private static Set<GeologicalAgeTO> convertToDTOs(Collection<GeologicalAge> geoAges) {
        return geoAges.stream().map(GeologicalAgeServiceImpl::convertToDTO).collect(Collectors.toSet());
    }
    
    private static GeologicalAgeTO convertToDTO(GeologicalAge geoAge) {
        return new GeologicalAgeTO(geoAge.getNotation(), geoAge.getName(), geoAge.getRank(),
                geoAge.getYoungerBound(), geoAge.getYoungerBoundImprecision(),
                geoAge.getOlderBound(), geoAge.getOlderBoundImprecision(), geoAge.getSynonyms());
    }
    
    private List<GeologicalAge> getGeologicalAges(List<GeologicalAgeTO> geologicalAgeTOs) {
        return geologicalAgeTOs.stream()
                       .map(ServiceUtils::mapFromTO)
                       .collect(Collectors.toList());
    }
}
