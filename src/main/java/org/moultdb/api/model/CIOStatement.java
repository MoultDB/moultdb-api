package org.moultdb.api.model;

/**
 * @author Valentine Rech de Laval
 * @since 2024-09-17
 */
public class CIOStatement extends NamedEntity<String> {
    
    public CIOStatement(String accession, String name) throws IllegalArgumentException {
        super(accession, name);
    }
    
    public CIOStatement(String accession, String name, String description) throws IllegalArgumentException {
        super(accession, name, description);
    }
}
