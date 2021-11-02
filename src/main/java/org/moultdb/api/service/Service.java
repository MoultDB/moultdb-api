package org.moultdb.api.service;

import org.moultdb.api.model.DataSource;
import org.moultdb.api.model.DatedTaxon;
import org.moultdb.api.model.DbXref;
import org.moultdb.api.model.EgressDirection;
import org.moultdb.api.model.GeneralMoultingCharacters;
import org.moultdb.api.model.GeologicalAge;
import org.moultdb.api.model.SutureLocation;
import org.moultdb.api.model.Taxon;
import org.moultdb.api.model.User;
import org.moultdb.api.model.Version;
import org.moultdb.api.repository.dto.DataSourceTO;
import org.moultdb.api.repository.dto.DatedTaxonTO;
import org.moultdb.api.repository.dto.DbXrefTO;
import org.moultdb.api.repository.dto.GeneralMoultingCharactersTO;
import org.moultdb.api.repository.dto.GeologicalAgeTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.api.repository.dto.VersionTO;

import java.util.Map;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-27
 */
public interface Service {
    
    static DataSource mapFromTO(DataSourceTO dataSourceTO) {
        return new DataSource(dataSourceTO.getName(), dataSourceTO.getDescription(),
                dataSourceTO.getBaseURL(), dataSourceTO.getReleaseDate(), dataSourceTO.getReleaseVersion());
    }
    
    static DatedTaxon mapFromTO(DatedTaxonTO datedTaxonTO, Map<Integer, GeneralMoultingCharactersTO> charactersTOsByDatedTaxonId,
                                Map<Integer, VersionTO> versionTOsById, Map<Integer, TaxonTO> taxonTOsById,
                                Map<Integer, DbXrefTO> dbXrefTOsById) {
        
        GeneralMoultingCharactersTO charactersTO = charactersTOsByDatedTaxonId.get(datedTaxonTO.getId());
        SutureLocation sl = new SutureLocation(charactersTO.getSutureLocationTO().getName());
        EgressDirection ed = new EgressDirection(charactersTO.getEgressDirectionTO().getName());
        Version characterVersion = Service.mapFromTO(versionTOsById.get(charactersTO.getVersionId()));
        
        Version datedTaxonVersion = Service.mapFromTO(versionTOsById.get(datedTaxonTO.getVersionId()));
        
        return new DatedTaxon(Service.mapFromTO(datedTaxonTO.getGeologicalAgeTO()),
                Service.mapFromTO(taxonTOsById.get(datedTaxonTO.getTaxonId()), dbXrefTOsById),
                Service.mapFromTO(charactersTO, sl, ed, characterVersion),
                datedTaxonVersion);
    }
    
    static DbXref mapFromTO(DbXrefTO dbXrefTO) {
        return new DbXref(dbXrefTO.getAccession(), mapFromTO(dbXrefTO.getDataSourceTO()));
    }
    
    static GeneralMoultingCharacters mapFromTO(GeneralMoultingCharactersTO charactersTO, SutureLocation sl,
                                               EgressDirection ed, Version characterVersion) {
        return new GeneralMoultingCharacters(charactersTO.getHemimetabolous(),
                charactersTO.getMoultCount(), charactersTO.getSizeIncrease(), charactersTO.getHasAdultStage(),
                charactersTO.getAnamorphic(), charactersTO.getHasFixedMoultNumber(), sl, ed,
                charactersTO.getFragmentedExuviae(), charactersTO.getMonoPhasicMoulting(),
                charactersTO.getHasExoskeletalMaterialReabsorption(), charactersTO.getHasExuviaeConsumed(),
                charactersTO.getRepairExtent(), charactersTO.getMassMoulting(), charactersTO.getMatingLinked(),
                charactersTO.getHormoneRegulation(), characterVersion);
    }
    
    static GeologicalAge mapFromTO(GeologicalAgeTO geologicalAgeTO) {
        return new GeologicalAge(geologicalAgeTO.getName(), geologicalAgeTO.getSymbol(),
                geologicalAgeTO.getYoungerBound(), geologicalAgeTO.getOlderBound());
    }
    
    static Taxon mapFromTO(TaxonTO taxonTO, Map<Integer, DbXrefTO> dbXrefTOsById) {
        return new Taxon(taxonTO.getName(), taxonTO.getCommonName(), mapFromTO(dbXrefTOsById.get(taxonTO.getDbXrefId())),
                taxonTO.getTaxonRank(), taxonTO.getParentTaxonId(), taxonTO.isExtinct(), taxonTO.getPath());
    }
    
    static Taxon mapFromTO(TaxonTO taxonTO,DbXrefTO dbXrefTO) {
        return new Taxon(taxonTO.getName(), taxonTO.getCommonName(), mapFromTO(dbXrefTO),
                taxonTO.getTaxonRank(), taxonTO.getParentTaxonId(), taxonTO.isExtinct(), taxonTO.getPath());
    }
    
    static Version mapFromTO(VersionTO versionTO) {
        return new Version(new User(versionTO.getCreationUserTO().getName()),
                versionTO.getCreationDate(), new User(versionTO.getLastUpdateUserTO().getName()),
                versionTO.getLastUpdateDate(), versionTO.getVersionNumber());
    }
}
