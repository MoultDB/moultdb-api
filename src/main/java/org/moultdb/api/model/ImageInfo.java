package org.moultdb.api.model;

import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2023-05-31
 */
public class ImageInfo extends NamedEntity<String> {
    
    private final String url;
    
    public ImageInfo(String id, String name, String url) {
        super(id, name);
        this.url = url;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", ImageInfo.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("name='" + getName() + "'")
                .add("url='" + url + "'")
                .toString();
    }
}
