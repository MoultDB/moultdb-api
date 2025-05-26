package org.moultdb.api.model.moutldbenum;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-22
 */
public enum MoultingStep implements MoutldbEnum {
    
    PRE_MOULT("pre-moult"),
    MOULTING("moulting"),
    INTER_MOULT("inter-moult"),
    POST_MOULT("post-moult"),
    MULTIPLE("multiple");
    
    private final String stringRepresentation;
    
    MoultingStep(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }
    
    @Override
    public String getStringRepresentation() {
        return this.stringRepresentation;
    }
    
    public static MoultingStep valueOfByStringRepresentation(String representation) {
        return MoutldbEnum.valueOfByStringRepresentation(MoultingStep.class, representation);
    }
}
