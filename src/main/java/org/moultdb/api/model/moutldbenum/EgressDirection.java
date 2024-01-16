package org.moultdb.api.model.moutldbenum;

/**
 * @author Valentine Rech de Laval
 * @since 2022-04-05
 */
public enum EgressDirection implements MoutldbEnum {
    
    ANTERIOR("anterior"),
    POSTERIOR("posterior");
    
    private final String stringRepresentation;
    
    EgressDirection(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }
    
    @Override
    public String getStringRepresentation() {
        return this.stringRepresentation;
    }
    
    public static EgressDirection valueOfByStringRepresentation(String representation) {
        return MoutldbEnum.valueOfByStringRepresentation(EgressDirection.class, representation);
    }
}
