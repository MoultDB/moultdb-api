package org.moultdb.api.model;

import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2023-05-31
 */
// TODO check whether an x-ref would be a better option than a simple url
public class Observation extends Entity<Integer> {
    
    private final String url;
    private final String description;
    
    public Observation(Integer id, String url, String description) {
        super(id);
        this.url = url;
        this.description = description;
    }
    
    public String getUrl() {
        return url;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Observation.class.getSimpleName() + "[", "]")
                .add("id=" + getId())
                .add("url='" + url + "'")
                .add("description='" + description + "'")
                .toString();
    }
}
