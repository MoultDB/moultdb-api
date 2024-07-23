package org.moultdb.api.model.moutldbenum;

/**
 * @author Valentine Rech de Laval
 * @since 2023-10-20
 */
public enum LifeMode implements MoutldbEnum {
    
    PELAGIC ("pelagic"),
    SESSILE ("sessile"),
    EPIFAUNAL ("epifaunal"),
    INFAUNAL ("infaunal"),
    BENTHIC("benthic"),
    EPIBENTHIC("epibenthic"),
    ERECT("erect"),
    SURFICIAL("surficial"),
    SEMI_INFAUNAL("semi-infaunal"),
    SHALLOW_INFAUNAL("shallow infaunal"),
    DEEP_INFAUNAL("deep infaunal"),
    MESOPELAGIC("mesopelagic");
    
    private final String stringRepresentation;
    
    LifeMode(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }
    
    @Override
    public String getStringRepresentation() {
        return this.stringRepresentation;
    }
    
    public static LifeMode valueOfByStringRepresentation(String representation) {
        return MoutldbEnum.valueOfByStringRepresentation(LifeMode.class, representation);
    }
}
