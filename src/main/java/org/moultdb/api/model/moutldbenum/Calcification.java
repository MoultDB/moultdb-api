package org.moultdb.api.model.moutldbenum;

/**
 * @author Valentine Rech de Laval
 * @since 2023-10-20
 */
public enum Calcification implements MoutldbEnum {
    
    NONE("none"),
    WEAK("weak"),
    STRONG("strong");
    
    private final String stringRepresentation;
    
    Calcification(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }
    
    @Override
    public String getStringRepresentation() {
        return this.stringRepresentation;
    }
    
    public static Calcification valueOfByStringRepresentation(String representation) {
        return MoutldbEnum.valueOfByStringRepresentation(Calcification.class, representation);
    }
}
