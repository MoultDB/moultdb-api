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
import org.moultdb.importer.ImportUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.Trim;
import org.supercsv.cellprocessor.constraint.StrNotNullOrEmpty;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.*;
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
    private final static String LIST_SEPARATOR = "; ";
    private final static String ID_COL_NAME = "id";
    private final static String PATH_COL_NAME = "Index";
    private final static String NCBI_ID_COL_NAME = "ncbi_id";
    private final static String GBIF_ID_COL_NAME = "gbif_taxon_id";
    private final static String NCBI_NAME_COL_NAME = "ncbi_canonicalName";
    private final static String GBIF_NAME_COL_NAME = "canonicalName";
    private final static String SYNONYM_GBIF_IDS_COL_NAME = "gbif_synonyms_ids";
    private final static String SYNONYM_GBIF_NAMES_COL_NAME = "gbif_synonyms_names";
    private final static String SYNONYM_NCBI_NAMES_COL_NAME = "ncbi_synonyms_names";
    
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
    
    public Set<TaxonTO> getTaxonTOs(Set<TaxonBean> taxonBeans, TaxonDAO taxonDAO, DataSourceDAO dataSourceDAO,
                                    DbXrefDAO dbXrefDAO) {

        Integer dbXrefLastId = dbXrefDAO.getLastId();
        Integer dbXrefNextId = dbXrefLastId == null ? 1 : dbXrefLastId + 1;
        
        DataSourceTO ncbiTO = dataSourceDAO.findByName(ImportUtils.NCBI_TAXONOMY_DATASOURCE_NAME);
        DataSourceTO gbifTO = dataSourceDAO.findByName(ImportUtils.GBIF_TAXONOMY_DATASOURCE_NAME);
        if (ncbiTO == null || gbifTO == null) {
            throw new IllegalArgumentException("Unknown data source(s).");
        }
        
        Set<TaxonTO> taxonTOs = new HashSet<>();
        for (TaxonBean bean: taxonBeans) {
            String scientificName = cleanName(bean.getNcbiName());
            if (scientificName == null) {
                scientificName = cleanName(bean.getGbifName());
            }
            
            TaxonTO taxonTO = taxonDAO.findByScientificName(scientificName);
            if (taxonTO != null) {
                logger.debug("Taxon scientific name already exits: " + scientificName);
                continue;
            }
            
            Set<DbXrefTO> dbXrefTOs = new HashSet<>();
            Set<TaxonToDbXrefTO> taxonToDbXrefTOs = new HashSet<>();
            dbXrefNextId = addDbXref(dbXrefDAO, dbXrefNextId, ncbiTO, cleanId(bean.getNcbiId()), cleanName(bean.getNcbiName()),
                    bean.getPath(), true, dbXrefTOs, taxonToDbXrefTOs);
            dbXrefNextId = addDbXref(dbXrefDAO, dbXrefNextId, gbifTO, cleanId(bean.getGbifId()), cleanName(bean.getGbifName()),
                    bean.getPath(), true, dbXrefTOs, taxonToDbXrefTOs);
            
            List<String> synonymGbifIds = extractValues(bean.getSynonymGbifIds());
            List<String> synonymGbifNames = extractValues(bean.getSynonymGbifNames());
            for (int i = 0; i < synonymGbifIds.size(); i++) {
                dbXrefNextId = addDbXref(dbXrefDAO, dbXrefNextId, gbifTO, cleanId(synonymGbifIds.get(i)),
                        cleanSynonym(synonymGbifNames.get(i)), bean.getPath(), false, dbXrefTOs, taxonToDbXrefTOs);
            }
            for (String ncbiName : extractValues(bean.getSynonymNcbiNames())) {
                dbXrefNextId = addDbXref(dbXrefDAO, dbXrefNextId, ncbiTO, null, cleanSynonym(ncbiName),
                        bean.getPath(), false, dbXrefTOs, taxonToDbXrefTOs);
            }
            
            taxonTOs.add(new TaxonTO(bean.getPath(), scientificName, null, null, null, dbXrefTOs, taxonToDbXrefTOs));
        }
        return taxonTOs;
    }
    
    private static String cleanId(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return id.split("\\.0")[0];
    }
    
    private static String cleanName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return name.trim();
    }
    
    private static String cleanSynonym(String synonym) {
        String s = cleanName(synonym);
        if (s == null) {
            throw new IllegalArgumentException("Synonym name cannot be null");
        }
        return s;
    }
    
    private static Integer addDbXref(DbXrefDAO dbXrefDAO, Integer dbXrefNextId, DataSourceTO sourceTO,
                                     String accession, String name, String path, boolean isMain,
                                     Set<DbXrefTO> dbXrefTOs, Set<TaxonToDbXrefTO> taxonToDbXrefTOs) {
        if (isMain && accession == null) {
            return dbXrefNextId;
        }
        DbXrefTO dbXrefTO = dbXrefDAO.find(accession, name, sourceTO.getId());
        if (dbXrefTO == null) {
            dbXrefTO = new DbXrefTO(dbXrefNextId, accession, name, sourceTO);
            dbXrefNextId++;
        }
        dbXrefTOs.add(dbXrefTO);
        
        taxonToDbXrefTOs.add(new TaxonToDbXrefTO(path, dbXrefTO.getId(), isMain));
        
        return dbXrefNextId;
    }
    
    private List<String> extractValues(String s) {
        if (StringUtils.isBlank(s)) {
            return new ArrayList<>();
        }
        return Arrays.stream(s.split(LIST_SEPARATOR))
                     .map(String::trim)
                     .collect(Collectors.toList());
    }
    
    public Set<TaxonBean> getTaxonBeans(MultipartFile uploadedFile) {
        try (ICsvBeanReader taxonReader = new CsvBeanReader(new InputStreamReader(uploadedFile.getInputStream()), TSV_COMMENTED)) {
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
                case PATH_COL_NAME
                        -> new StrNotNullOrEmpty(new Trim());
                case NCBI_NAME_COL_NAME, GBIF_NAME_COL_NAME, 
                        SYNONYM_GBIF_IDS_COL_NAME, SYNONYM_GBIF_NAMES_COL_NAME, SYNONYM_NCBI_NAMES_COL_NAME
                        -> new Optional(new Trim());
                case NCBI_ID_COL_NAME, GBIF_ID_COL_NAME
                        -> new NegativeIsEmptyOptional(new Trim());
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
                case NCBI_ID_COL_NAME -> "ncbiId";
                case GBIF_ID_COL_NAME -> "gbifId";
                case NCBI_NAME_COL_NAME -> "ncbiName";
                case GBIF_NAME_COL_NAME -> "gbifName";
                case SYNONYM_GBIF_IDS_COL_NAME -> "synonymGbifIds";
                case SYNONYM_GBIF_NAMES_COL_NAME -> "synonymGbifNames";
                case SYNONYM_NCBI_NAMES_COL_NAME -> "synonymNcbiNames";
                default -> throw new IllegalArgumentException("Unrecognized header: " + header[i] + " for TaxonBean");
            };
        }
        return mapping;
    }

    public static class NegativeIsEmptyOptional extends CellProcessorAdaptor {

        public NegativeIsEmptyOptional() {
            super();
        }

        public NegativeIsEmptyOptional(CellProcessor next) {
            // this constructor allows other processors to be chained after this processor
            super(next);
        }

        public Object execute(Object value, CsvContext context) {
            String stringValue = String.valueOf(value);
            if (StringUtils.isBlank(stringValue) || stringValue.equals("-1")) {
                return null;
            }
            return next.execute(value, context);
        }
    }
}
