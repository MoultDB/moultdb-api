package org.moultdb.api.model;

import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2023-05-31
 */
public class ImageInfo {
    
    private String name;
    private String url;
    
    public ImageInfo(String name, String url) {
        this.name = name;
        this.url = url;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", ImageInfo.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("url='" + url + "'")
                .toString();
    }
}
