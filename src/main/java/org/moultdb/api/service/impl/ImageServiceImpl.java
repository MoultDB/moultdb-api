package org.moultdb.api.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.moultdb.api.controller.ImageController;
import org.moultdb.api.exception.ImageUploadException;
import org.moultdb.api.exception.MoultDBException;
import org.moultdb.api.model.ImageInfo;
import org.moultdb.api.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Valentine Rech de Laval
 * @since 2023-05-31
 */
@Service
public class ImageServiceImpl implements ImageService {
    
    @Value("${file.storage}")
    private String FILE_STORAGE;
    
    private final long FILE_MAX_SIZE = 1 * 1024 * 1024; // 1 Mo
    private final Set<String> ALLOWED_CONTENT_TYPES = new HashSet<>(Arrays.asList("image/jpeg", "image/png"));
    
    private Path getPath() {
        return Paths.get(FILE_STORAGE);
    }
    
    @Override
    public String getAbsolutePath() {
        return Paths.get(FILE_STORAGE).toFile().getAbsolutePath();
    }
    
    @Override
    public void saveImage(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            throw new IllegalArgumentException("Filename cannot be blank.");
        }
        if (file.getSize() > FILE_MAX_SIZE) {
            throw new MaxUploadSizeExceededException(FILE_MAX_SIZE);
        }
        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new ImageUploadException("Format file cannot be other than: " + String.join(", ", ALLOWED_CONTENT_TYPES));
        }
    
        try {
            Files.copy(file.getInputStream(), getPath().resolve(generateImageFileName(originalFilename)));
        } catch (FileAlreadyExistsException e) {
            throw new MoultDBException("A file of that name already exists.");
        } catch (Exception e) {
            throw new MoultDBException(e.getMessage());
        }
    }
    
    private static String generateImageFileName(String filename) {
        String extension = "";
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            extension = filename.substring(dotIndex).toLowerCase();
        } else {
            throw new ImageUploadException("Filename should have an extension.");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        
        return "image_" + timestamp + extension;
    }
    
    @Override
    public Resource getImage(String filename) {
        try {
            Path file = getPath().resolve(filename);
            Resource resource = new UrlResource(file.toUri());
        
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new ImageUploadException("Could not read the file.");
            }
        } catch (MalformedURLException e) {
            throw new ImageUploadException("Error: " + e.getMessage());
        }
    }
    
    @Override
    public List<ImageInfo> getAllImages() {
        try (Stream<Path> entries = Files.walk(getPath(), 1)) {
            return entries
                    .filter(path -> !path.equals(getPath()))
                    .map(path -> getPath().relativize(path))
                    .map(path -> {
                        String filename = path.getFileName().toString();
                        String url = MvcUriComponentsBuilder
                                .fromMethodName(ImageController.class, "getImage", path.getFileName().toString()).build().toString();
    
                        return new ImageInfo(filename, url);
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new ImageUploadException("Could not load the files!");
        }
    }
    
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(getPath().toFile());
    }
}
