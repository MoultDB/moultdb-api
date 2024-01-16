package org.moultdb.api.model.moutldbenum;

/**
 * @author Valentine Rech de Laval
 * @since 2023-10-20
 */
public enum LifeMode implements MoutldbEnum {
    
    PELAGIC ("pelagic"),
    SESSILE ("sessile"),
    EPIFAUNAL ("epifaunal"),
    INFAUNAL ("infaunal");
    
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
