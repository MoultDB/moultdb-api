package org.moultdb.api.model;

import org.springframework.data.relational.core.sql.In;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2021-12-14
 */
public enum MoultdbKeywords {
    
    FOSSIL_PRESERVATION_TYPE(Set.of("secondarily phosphatised", "internal and external moulds", "carbonaceous compression", "silicified", "amber", "internal mould", "external mould", "cast", "lens", "concretion", "nodule", "soft tissue", "recrystallised", "carbonised/coalified", "bioimmured", "trace", "subfossil", "phosphatised")),
    ENVIRONMENT(Set.of("terrestrial", "marine", "estuarine", "lacustrine", "desert", "rainforest", "woodland", "polar", "riverine", "coral reef")),
    SPECIMEN_TYPE(Set.of("moult ensemble", "collection of specimens", "in moulting nest", "during moulting", "fossil", "observations of living individuals", "observations of empty fresh moults/carcasses", "mid-moult", "ontogenetic sequence", "preserved moult assemblage", "collection of wild living specimens", "aquarium-reared specimens", "larvae")),
    LIFE_HISTORY_STYLE(Set.of("metamorphosis", "direct development", "heterometabolism", "holometabolism", "hemimetabolism")),
    SEGMENT_ADDITION_MODE(Set.of("anamorphic", "euanamorphosis", "teloanamorphosis", "hemianamorphosis", "epimorphosis", "desegmentation", "abdominal segments released into thorax", "pygidial segments released into thorax", "anterior growth zone", "posterior growth zone", "biphasic")),
    MOULTING_SUTURE_LOCATION(Set.of("dorsal", "ventral", "cephalon", "cephalothoracic joint", "post-cephalic")),
    CEPHALIC_SUTURE_LOCATION(Set.of("facial suture", "rostral suture", "ventral sutures", "marginal cephalon suture", "hypostomal suture", "prosomal suture")),
    POST_CEPHALIC_SUTURE_LOCATION(Set.of("cephalothorax", "between body segments", "thorax-tail shield joint", "midline of carapace", "anterior of carapace", "posterior of carapace", "marginal carapace suture")),
    RESULTING_NAMED_MOULTING_CONFIGURATIONS(Set.of("lobster open moult position", "crab open moult position", "overturned-carapace moult configuration henningsmoen's configuration", "harrington's configuration", "axial shield", "mcnamara's configuration", "somersault configuration", "salter's configuration", "zombie configuration", "nutcracker configuration", "marginal gape")),
    EGRESS_DIRECTION_DURING_MOULTING(Set.of("anterior", "posterior", "dorsal", "ventral")),
    POSITION_EXUVIAE_FOUND_IN(Set.of("prone", "supine", "prone/supine")),
    MOULTING_PHASE(Set.of("monophasic", "biphasic")),
    MOULTING_VARIABILITY(Set.of("none", "some", "lots")),
    JUVENILE_MOULTING_BEHAVIOURS(Set.of("same as in adults", "different to in adults")),
    OTHER_BEHAVIOURS_ASSOCIATED_WITH_MOULTING(Set.of("synchronous", "mass moulting", "mating", "moulting nest", "burrowing", "hiding outside a burrow", "enrollment", "partial enrollment", "association with other species", "moult retention")),
    EXUVIAE_CONSUMPTION(Set.of("yes", "no", "partially")),
    REABSORPTION(Set.of("yes", "no", "partial", "calcium reabsorption")),
    FOSSIL_EXUVIAE_QUALITY(Set.of("fragmented/fractured", "incomplete configuration", "complete configuration", "cephalon missing", "post-cephalon missing", "tectonically/metamorphically deformed"));
    
    private final Set<String> allowedValues;
    
    MoultdbKeywords(Set<String> allowedValues) {
        this.allowedValues = allowedValues;
    }
    
    public Set<String> getAllowedValues() {
        return allowedValues;
    }
    
    public List<Object> getAllowedValuesAsObjects() {
        return new ArrayList<>(allowedValues);
    }
    
}
