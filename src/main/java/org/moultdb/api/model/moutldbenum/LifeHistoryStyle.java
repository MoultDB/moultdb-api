package org.moultdb.api.model.moutldbenum;

/**
 * @author Valentine Rech de Laval
 * @since 2022-04-05
 */
public enum LifeHistoryStyle implements MoutldbEnum {
    
    DIRECT_DEVELOPMENT("direct development"),
    HETEROMETABOLISM("heterometabolism"),
    HOLOMETABOLISM("holometabolism"),
    HEMIMETABOLISM("hemimetabolism");
    
    private final String stringRepresentation;
    
    LifeHistoryStyle(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }
    
    @Override
    public String getStringRepresentation() {
        return this.stringRepresentation;
    }
}
