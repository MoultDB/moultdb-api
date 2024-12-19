package org.moultdb.api.model;

import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2023-05-31
 */
// TODO check whether an x-ref would be a better option than a simple url
public class ImageInfo extends Entity<String> {
    
    private final String description;
    
    public ImageInfo(String url, String description) {
        super(url);
        this.description = description;
    }
    
    public String getUrl() {
        return this.getId();
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", ImageInfo.class.getSimpleName() + "[", "]")
                .add("url='" + getUrl() + "'")
                .add("description='" + getDescription() + "'")
                .toString();
    }
}
