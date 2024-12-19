package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public class ImageTO extends EntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = -4777731583190160998L;
    
    private final String fileName;
    
    private final String url;
    
    private final String description;
    
    public ImageTO(Integer id, String fileName, String url, String description)
            throws IllegalArgumentException {
        super(id);
        this.fileName = fileName;
        this.url = url;
        this.description = description;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public String getUrl() {
        return url;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", ImageTO.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("fileName='" + fileName + "'")
                .add("url='" + url + "'")
                .add("description='" + description + "'")
                .toString();
    }
}
