package org.moultdb.api.model.moutldbenum;

/**
 * @author Valentine Rech de Laval
 * @since 2022-04-05
 */
public enum Reabsorption implements MoutldbEnum {
    
    YES("reabsorption of exoskeleton material"),
    NO("no reabsorption of exoskeleton material"),
    PARTIAL("partial reabsorption of exoskeleton material"),
    CALCIUM_REABSORPTION("calcium reabsorption"),
    ENDOCUTICLE_REABSORPTION("reabsorption of endocuticle");
    
    private final String stringRepresentation;
    
    Reabsorption(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }
    
    @Override
    public String getStringRepresentation() {
        return this.stringRepresentation;
    }
    
    public static Reabsorption valueOfByStringRepresentation(String representation) {
        return MoutldbEnum.valueOfByStringRepresentation(Reabsorption.class, representation);
    }
}
