package org.moultdb.api.model.moutldbenum;

/**
 * @author Valentine Rech de Laval
 * @since 2023-07-03
 */
public enum Sex implements MoutldbEnum {
    
    UNKNOWN("unknown"),
    HERMAPHRODITE("'hermaphrodite'"),
    FEMALE("female"),
    MALE("male");
    
    private final String stringRepresentation;
    
    Sex(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }
    
    @Override
    public String getStringRepresentation() {
        return this.stringRepresentation;
    }
    
    public static Sex valueOfByStringRepresentation(String representation) {
        return MoutldbEnum.valueOfByStringRepresentation(Sex.class, representation);
    }
}
