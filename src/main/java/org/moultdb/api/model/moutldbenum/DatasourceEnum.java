package org.moultdb.api.model.moutldbenum;

/**
 * @author Valentine Rech de Laval
 * @since 2023-11-22
 */
public enum DatasourceEnum implements MoutldbEnum {
    
    NCBI("NCBI Taxonomy"),
    GBIF("GBIF Backbone Taxonomy"),
    GENBANK("GenBank");
    
    private final String stringRepresentation;
    
    DatasourceEnum(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }
    
    @Override
    public String getStringRepresentation() {
        return this.stringRepresentation;
    }
    
    public static DatasourceEnum valueOfByStringRepresentation(String representation) {
        return MoutldbEnum.valueOfByStringRepresentation(DatasourceEnum.class, representation);
    }
}
