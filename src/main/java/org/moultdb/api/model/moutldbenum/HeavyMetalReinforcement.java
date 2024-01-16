package org.moultdb.api.model.moutldbenum;

/**
 * @author Valentine Rech de Laval
 * @since 2023-10-20
 */
public enum HeavyMetalReinforcement implements MoutldbEnum {
    ZN("Zn"),
    MN("Mn"),
    FE("Fe"),
    NI("Ni");
    
    private final String stringRepresentation;
    
    HeavyMetalReinforcement(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }
    
    @Override
    public String getStringRepresentation() {
        return this.stringRepresentation;
    }
    
    public static HeavyMetalReinforcement valueOfByStringRepresentation(String representation) {
        return MoutldbEnum.valueOfByStringRepresentation(HeavyMetalReinforcement.class, representation);
    }
}
