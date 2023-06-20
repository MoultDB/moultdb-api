package org.moultdb.api.model.moutldbenum;

/**
 * @author Valentine Rech de Laval
 * @since 2022-04-05
 */
public enum MoultingPhase implements MoutldbEnum {
    
    MONOPHASIC("monophasic"),
    BIPHASIC("biphasic"),
    MULTIPHASIC("multiphasic");
    
    private final String stringRepresentation;
    
    MoultingPhase(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }
    
    @Override
    public String getStringRepresentation() {
        return this.stringRepresentation;
    }
    
    public static MoultingPhase valueOfByStringRepresentation(String representation) {
        return MoutldbEnum.valueOfByStringRepresentation(MoultingPhase.class, representation);
    }
}
