package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public class ObservationTO extends EntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = -4777731583190160998L;
    
    private final String url;
    
    private final String description;
    
    public ObservationTO(Integer id, String url, String description)
            throws IllegalArgumentException {
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
        return new StringJoiner(", ", ObservationTO.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("url='" + url + "'")
                .add("description='" + description + "'")
                .toString();
    }
}
