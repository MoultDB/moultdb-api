package org.moultdb.api.model;

/**
 * @author Valentine Rech de Laval
 * @since 2022-04-11
 */
public class AnatEntity extends NamedEntity<String> {
    
    public AnatEntity(String accession, String name) throws IllegalArgumentException {
        super(accession, name);
    }
    
    public AnatEntity(String accession, String name, String description) throws IllegalArgumentException {
        super(accession, name, description);
    }
}
