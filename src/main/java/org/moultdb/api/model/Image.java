package org.moultdb.api.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author Valentine Rech de Laval
 * @since 2023-06-20
 */
public class Image {
    
    private final String fileName;

    private final String filePath;
    
    private final String description;
    
    public Image(String fileName, String filePath)  {
        this(fileName, filePath, null);
    }
    
    public Image(String fileName, String filePath, String description)  {
        if (StringUtils.isBlank(fileName) || StringUtils.isBlank(filePath)) {
            throw new IllegalArgumentException("The file name or the file path cannot be blank");
        }
        this.fileName = fileName;
        this.filePath = filePath;
        this.description = description;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(filePath, image.filePath);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(filePath);
    }
}
