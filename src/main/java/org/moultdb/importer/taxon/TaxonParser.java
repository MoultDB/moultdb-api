package org.moultdb.importer.taxon;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.DataSourceDAO;
import org.moultdb.api.repository.dao.DbXrefDAO;
import org.moultdb.api.repository.dao.TaxonDAO;
import org.moultdb.api.repository.dto.DataSourceTO;
import org.moultdb.api.repository.dto.DbXrefTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.api.repository.dto.TaxonToDbXrefTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.Trim;
import org.supercsv.cellprocessor.constraint.StrNotNullOrEmpty;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2021-12-13
 */
public class TaxonParser {
    
    @Autowired TaxonDAO taxonDAO;
    @Autowired DataSourceDAO dataSourceDAO;
    @Autowired DbXrefDAO dbXrefDAO;
    
    private final static Logger logger = LogManager.getLogger(TaxonParser.class.getName());
    
    private final static CsvPreference TSV_COMMENTED = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE).build();
    
    private final static String ID_COL_NAME = "id";
    private final static String PATH_COL_NAME = "path";
    private final static String NCBI_ID_COL_NAME = "ncbi_id";
    private final static String GBIF_ID_COL_NAME = "gbif_id";
    private final static String SCIENTIFIC_NAME_COL_NAME = "scientific_name";
    private final static String NCBI_RANK_COL_NAME = "ncbi_rank";
    private final static String GBIF_RANK_COL_NAME = "gbif_rank";
    private final static String SYNONYM_GBIF_IDS_COL_NAME = "synonym_GBIF_ids";
    private final static String SYNONYM_GBIF_NAMES_COL_NAME = "synonym_GBIF_names";
    
    public static void main(String[] args) {
        logger.traceEntry(Arrays.toString(args));
        
        if (args.length != 1) {
            throw new IllegalArgumentException("Incorrect number of arguments provided, expected 1 argument, " +
                    args.length + " provided.");
        }
    
        TaxonParser parser = new TaxonParser();
        Set<TaxonBean> taxonBeans = parser.parseTaxonFile(args[0]);
        parser.getTaxonTOs(taxonBeans);
        
        logger.traceExit();
    }
    
    public Set<TaxonBean> parseTaxonFile(String fileName) {
        logger.info("Start parsing of taxon file " + fileName + "...");
        
        try (ICsvBeanReader taxonReader = new CsvBeanReader(new FileReader(fileName), TSV_COMMENTED)) {
            
            logger.info("End parsing of taxon file");
            
            return logger.traceExit(getTaxonBeans(taxonReader));
            
        } catch (SuperCsvException e) {
            throw new IllegalArgumentException("The provided file " + fileName + " could not be properly parsed", e);
        } catch (IOException e) {
            throw new UncheckedIOException("Can not read file " + fileName, e);
        }
    }

    public Set<TaxonTO> getTaxonTOs(Set<TaxonBean> taxonBeans) {
        return getTaxonTOs(taxonBeans, taxonDAO, dataSourceDAO, dbXrefDAO);
    }
    
    public Set<TaxonTO> getTaxonTOs(Set<TaxonBean> taxonBeans, TaxonDAO taxonDAO, DataSourceDAO dataSourceDAO, DbXrefDAO dbXrefDAO) {

        Integer dbXrefLastId = dbXrefDAO.getLastId();
        Integer dbXrefNextId = dbXrefLastId == null ? 1 : dbXrefLastId + 1;
        
        DataSourceTO ncbiTO = dataSourceDAO.findByName("NCBI Taxonomy");
        DataSourceTO gbifTO = dataSourceDAO.findByName("GBIF Backbone Taxonomy");
        if (ncbiTO == null || gbifTO == null) {
            throw new IllegalArgumentException("Unknown data source(s).");
        }
        
        Set<TaxonTO> taxonTOs = new HashSet<>();
        for (TaxonBean bean: taxonBeans) {

            TaxonTO taxonTO = taxonDAO.findByScientificName(bean.getScientificName());
            if (taxonTO != null) {
                logger.debug("Taxon scientific name already exits: " + bean.getScientificName());
                continue;
            }
            
            String rank = bean.getNcbiRank();
            if (StringUtils.isBlank(rank)) {
                rank = bean.getGbifRank();
            }
            
            Set<DbXrefTO> dbXrefTOs = new HashSet<>();
            Set<TaxonToDbXrefTO> taxonToDbXrefTOs = new HashSet<>();
            dbXrefNextId = addDbXref(dbXrefDAO, dbXrefNextId, ncbiTO, bean.getNcbiId(), bean.getPath(), true, dbXrefTOs, taxonToDbXrefTOs);
            dbXrefNextId = addDbXref(dbXrefDAO, dbXrefNextId, gbifTO, bean.getGbifId(), bean.getPath(), true, dbXrefTOs, taxonToDbXrefTOs);
            for (String gbifId : extractValues(bean.getSynonymGbifIds())) {
                dbXrefNextId = addDbXref(dbXrefDAO, dbXrefNextId, gbifTO, gbifId, bean.getPath(), false, dbXrefTOs, taxonToDbXrefTOs);
            }
            
            taxonTOs.add(new TaxonTO(bean.getPath(), bean.getScientificName(), null, null, rank, null, dbXrefTOs, taxonToDbXrefTOs));
        }
        return taxonTOs;
    }
    
    private static Integer addDbXref(DbXrefDAO dbXrefDAO, Integer dbXrefNextId, DataSourceTO sourceTO,
                                     String id, String path, boolean isMain,
                                     Set<DbXrefTO> dbXrefTOs, Set<TaxonToDbXrefTO> taxonToDbXrefTOs) {
        if (id == null) {
            return dbXrefNextId;
        }
        DbXrefTO dbXrefTO = dbXrefDAO.findByAccessionAndDatasource(id, sourceTO.getId());
        if (dbXrefTO == null) {
            dbXrefTO = new DbXrefTO(dbXrefNextId, id, sourceTO);
            dbXrefNextId++;
        }
        dbXrefTOs.add(dbXrefTO);
        
        taxonToDbXrefTOs.add(new TaxonToDbXrefTO(path, dbXrefTO.getId(), isMain));
        
        return dbXrefNextId;
    }
    
    private Set<String> extractValues(String s) {
        if (StringUtils.isBlank(s)) {
            return new HashSet<>();
        }
        return Arrays.stream(s.split(", "))
                     .map(String::trim)
                     .collect(Collectors.toSet());
    }
    
    public Set<TaxonBean> getTaxonBeans(MultipartFile uploadedFile) {
        logger.info("Start parsing of taxon file " + uploadedFile.getOriginalFilename() + "...");
    
        try (ICsvBeanReader taxonReader = new CsvBeanReader(new InputStreamReader(uploadedFile.getInputStream()), TSV_COMMENTED)) {
    
            logger.info("End parsing of taxon file");
    
            return logger.traceExit(getTaxonBeans(taxonReader));
        
        } catch (SuperCsvException e) {
            throw new IllegalArgumentException("The provided file " + uploadedFile.getOriginalFilename()
                    + " could not be properly parsed", e);
        } catch (IOException e) {
            throw new UncheckedIOException("Can not read file " + uploadedFile.getOriginalFilename(), e);
        }
    }
    
    private Set<TaxonBean> getTaxonBeans(ICsvBeanReader taxonReader) throws IOException {
        Set<TaxonBean> taxonBeans = new HashSet<>();
        
        final String[] header = taxonReader.getHeader(true);
        
        String[] attributeMapping = mapTaxonHeaderToAttributes(header);
        CellProcessor[] cellProcessorMapping = mapTaxonHeaderToCellProcessors(header);
        TaxonBean taxonBean;
        
        while((taxonBean = taxonReader.read(TaxonBean.class, attributeMapping, cellProcessorMapping)) != null) {
            taxonBeans.add(taxonBean);
        }
        if (taxonBeans.isEmpty()) {
            throw new IllegalArgumentException("The provided file did not allow to retrieve any taxonBean");
        }
        
        return taxonBeans;
    }
    
    private CellProcessor[] mapTaxonHeaderToCellProcessors(String[] header) {
        CellProcessor[] processors = new CellProcessor[header.length];
        for (int i = 0; i < header.length; i++) {
            processors[i] = switch (header[i]) {
                case ID_COL_NAME
                        -> new ParseInt();
                case PATH_COL_NAME, SCIENTIFIC_NAME_COL_NAME
                        -> new StrNotNullOrEmpty(new Trim());
                case NCBI_ID_COL_NAME, GBIF_ID_COL_NAME, NCBI_RANK_COL_NAME, GBIF_RANK_COL_NAME,
                        SYNONYM_GBIF_IDS_COL_NAME, SYNONYM_GBIF_NAMES_COL_NAME
                        -> new Optional(new Trim());
                default -> throw new IllegalArgumentException("Unrecognized header: " + header[i] + " for TaxonBean");
            };
        }
        return processors;
    }
    
    private String[] mapTaxonHeaderToAttributes(String[] header) {
        String[] mapping = new String[header.length];
        for (int i = 0; i < header.length; i++) {
            mapping[i] = switch (header[i]) {
                case ID_COL_NAME -> "id";
                case PATH_COL_NAME -> "path";
                case SCIENTIFIC_NAME_COL_NAME -> "scientificName";
                case NCBI_ID_COL_NAME -> "ncbiId";
                case GBIF_ID_COL_NAME -> "gbifId";
                case NCBI_RANK_COL_NAME -> "ncbiRank";
                case GBIF_RANK_COL_NAME -> "gbifRank";
                case SYNONYM_GBIF_IDS_COL_NAME -> "synonymGbifIds";
                case SYNONYM_GBIF_NAMES_COL_NAME -> "synonymGbifNames";
                default -> throw new IllegalArgumentException("Unrecognized header: " + header[i] + " for TaxonBean");
            };
        }
        return mapping;
    }
}
