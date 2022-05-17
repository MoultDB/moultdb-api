package org.moultdb.api.model.moutldbenum;

/**
 * @author Valentine Rech de Laval
 * @since 2022-04-05
 */
public enum Reabsorption implements MoutldbEnum {
    
    YES("yes"),
    NO("no"),
    PARTIAL("partial"),
    CALCIUM_REABSORPTION("calcium reabsorption");
    
    private final String stringRepresentation;
    
    Reabsorption(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }
    
    @Override
    public String getStringRepresentation() {
        return this.stringRepresentation;
    }
}
