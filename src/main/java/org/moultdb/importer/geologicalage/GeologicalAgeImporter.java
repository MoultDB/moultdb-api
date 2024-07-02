package org.moultdb.importer.geologicalage;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dto.GeologicalAgeTO;
import org.moultdb.importer.geologicalage.domain.AltLabelItem;
import org.moultdb.importer.geologicalage.domain.CommentItem;
import org.moultdb.importer.geologicalage.domain.NarrowerItem;
import org.moultdb.importer.geologicalage.domain.PrefLabelItem;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Valentine Rech de Laval
 * @since 2022-01-18
 */
public class GeologicalAgeImporter {
    
    private final static Logger logger = LogManager.getLogger(GeologicalAgeImporter.class.getName());
    
    private final static Set<String> AGES_TO_IMPORT = Stream.of("Precambrian", "Phanerozoic")
                                                            .collect(Collectors.toCollection(HashSet::new));
    
    private final static String DATA_PREFIX_URL = "https://vocabs.ardc.edu.au/repository/api/lda/csiro/" +
            "international-chronostratigraphic-chart/geologic-time-scale-2020/resource.json?" +
            "uri=http://resource.geosciml.org/classifier/ics/ischart/";
    
    public static void main(String[] args) {
        logger.traceEntry(Arrays.toString(args));
        
        if (args.length != 0){
            throw new IllegalArgumentException("Incorrect number of arguments provided, expected 0 arguments, " +
                    args.length + " provided");
        }
        
        GeologicalAgeImporter parser = new GeologicalAgeImporter();
        Set<GeologicalAgeTO> geologicalAgeTOs = parser.getGeologicalAgeTOs();
        
        logger.traceExit(geologicalAgeTOs);
    }
    
    public Set<GeologicalAgeTO> getGeologicalAgeTOs() {
        Set<GeologicalAgeTO> ageTOs = new HashSet<>();
        for (String ageLabel: AGES_TO_IMPORT) {
            ageTOs.addAll(getNarrowerAges(ageLabel));
        }
        return ageTOs;
    }
    
    private Set<GeologicalAgeTO> getNarrowerAges(String label) {
        logger.trace(label);
        
        HttpURLConnection conn = null;
    
        Set<GeologicalAgeTO> set = new HashSet<>();
        try {
            URL url = new URL(DATA_PREFIX_URL + label);
            
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            
            //Getting the response code
            int responseCode = conn.getResponseCode();
            
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                JsonReader json = new JsonReader(new InputStreamReader(conn.getInputStream()));
                json.setLenient(false);
                set.addAll(getGeologicalAgeTOs(json));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return set;
    }
    
    private Set<GeologicalAgeTO> getGeologicalAgeTOs(JsonReader json) {
        Set<GeologicalAgeTO> geologicalAgeTOs = new HashSet<>();
        Gson gson = new Gson(); //Create a Gson object
        IcsGeologicalAgeResponse fromJson = gson.fromJson(json, IcsGeologicalAgeResponse.class);
        
        String currentLabel = fromJson.getResult().getPrimaryTopic().getLabel().getValue();
        List<AltLabelItem> altLabelItems = fromJson.getResult().getPrimaryTopic().getAltLabel();
        Set<String> altLabelStrings = new HashSet<>();
        if (altLabelItems != null) {
            altLabelStrings.addAll(altLabelItems.stream()
                                                .filter(a -> a.getLang().equals("en"))
                                                .map(AltLabelItem::getValue)
                                                .collect(Collectors.toSet()));
            
        }
        List<NarrowerItem> narrowers = fromJson.getResult().getPrimaryTopic().getNarrower();
        if (narrowers != null) {
            for (NarrowerItem narrower: narrowers) {
                String narrowerLink = narrower.getAbout();
                String[] narrowerSplit = narrowerLink.split("/");
                geologicalAgeTOs.addAll(getNarrowerAges(narrowerSplit[narrowerSplit.length - 1]));
            }
        }
        List<String> prefLabels = fromJson.getResult().getPrimaryTopic().getPrefLabel().stream()
                                    .filter(l -> l.getLang().equals("en"))
                                    .map(PrefLabelItem::getValue)
                                    .collect(Collectors.toList());
        if (prefLabels.size() > 1) {
            logger.warn("Several pref. labels in english, we use the first one as geological age name");
        } else if (prefLabels.size() == 0) {
            throw new IllegalArgumentException("Missing pref. label in english");
        }
        String[] rankSplit = fromJson.getResult().getPrimaryTopic().getRank().split("/");
        String rank = rankSplit[rankSplit.length - 1];
    
        BigDecimal olderBound = null;
        BigDecimal olderBoundImprecision = null;
        BigDecimal youngerBound = null;
        BigDecimal youngerBoundImprecision = null;
        List<CommentItem> comments = fromJson.getResult().getPrimaryTopic().getComment();
        for (CommentItem commentItem: comments) {
            Pattern pattern = Pattern.compile("^-?([0-9]+[\\.0-9]*)( \\+\\|-([0-9]+[\\.0-9]*))? Ma$"); // ex: older bound -541.0 +|-1.0 Ma
            
            if (commentItem.getValue().startsWith("older bound")) {
                Matcher matcher = pattern.matcher(commentItem.getValue().substring(12));
                if (matcher.matches()) {
                    olderBound = new BigDecimal(matcher.group(1));
                    if (matcher.group(3) != null) {
                        olderBoundImprecision = new BigDecimal(matcher.group(3));
                    }
                }
                
            } else if (commentItem.getValue().startsWith("younger bound")) {
                Matcher matcher = pattern.matcher(commentItem.getValue().substring(14));
                if (matcher.matches()) {
                    youngerBound = new BigDecimal(matcher.group(1));
                    if (matcher.group(3) != null) {
                        youngerBoundImprecision = new BigDecimal(matcher.group(3));
                    }
                }
    
            }
        }
        String notation = fromJson.getResult().getPrimaryTopic().getNotation();
        
        GeologicalAgeTO geologicalAgeTO = new GeologicalAgeTO(notation, prefLabels.get(0), rank,
                youngerBound, youngerBoundImprecision, olderBound, olderBoundImprecision,
                altLabelStrings.size() == 0 ? null : altLabelStrings);
        if (geologicalAgeTO.getNotation() == null || geologicalAgeTO.getName() == null
                || geologicalAgeTO.getRank() == null|| geologicalAgeTO.getYoungerBound() == null
                || geologicalAgeTO.getOlderBound() == null) {
            throw new IllegalArgumentException("Notation, name, rank, younger bound or older bound cannot be null: " + geologicalAgeTO);
        }
        geologicalAgeTOs.add(geologicalAgeTO);

        logger.debug(notation + "\t" + currentLabel
                + "\t" + (altLabelStrings.size() == 0? null : String.join(";", altLabelStrings))
                + "\t" + String.join("; ", prefLabels) + "\t" + rank
                + "\t" + olderBound + "\t" + olderBoundImprecision
                + "\t" + youngerBound + "\t" + youngerBoundImprecision);
        return geologicalAgeTOs;
    }
}
