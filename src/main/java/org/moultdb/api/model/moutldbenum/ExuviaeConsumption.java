package org.moultdb.api.model.moutldbenum;

/**
 * @author Valentine Rech de Laval
 * @since 2022-04-05
 */
public enum ExuviaeConsumption implements MoutldbEnum {
    
    YES("yes"),
    NO("no"),
    PARTIAL("partial");
    
    private final String stringRepresentation;
    
    ExuviaeConsumption(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }
    
    @Override
    public String getStringRepresentation() {
        return this.stringRepresentation;
    }
}
