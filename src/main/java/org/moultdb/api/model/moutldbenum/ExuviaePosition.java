package org.moultdb.api.model.moutldbenum;

/**
 * @author Valentine Rech de Laval
 * @since 2022-04-05
 */
public enum ExuviaePosition implements MoutldbEnum {
    
    PRONE("prone"),
    SUPINE("supine"),
    SIDE("side");
    
    private final String stringRepresentation;
    
    ExuviaePosition(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }
    
    @Override
    public String getStringRepresentation() {
        return this.stringRepresentation;
    }
}
