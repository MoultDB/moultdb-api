package org.moultdb.api.model.moutldbenum;

/**
 * @author Valentine Rech de Laval
 * @since 2022-04-05
 */
public enum MoultingVariability implements MoutldbEnum {
    
    NO("no"),
    Yes("yes");
    
    private final String stringRepresentation;
    
    MoultingVariability(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }
    
    @Override
    public String getStringRepresentation() {
        return this.stringRepresentation;
    }
    
    public static MoultingVariability valueOfByStringRepresentation(String representation) {
        return MoutldbEnum.valueOfByStringRepresentation(MoultingVariability.class, representation);
    }
}
