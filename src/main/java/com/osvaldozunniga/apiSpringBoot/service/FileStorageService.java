package com.osvaldozunniga.apiSpringBoot.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.osvaldozunniga.apiSpringBoot.exception.FileStorageException;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".webp", ".gif");

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${file.upload-dir:uploads}") String uploadDirectoryPath) {
        this.fileStorageLocation = Paths.get(uploadDirectoryPath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ioException) {
            logger.error("Could not create upload directory at path: {}", uploadDirectoryPath, ioException);
            throw new FileStorageException("No se pudo crear el directorio de subida de archivos: " + uploadDirectoryPath, ioException);
        }
    }

    public String storeFile(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("No se puede almacenar un archivo vacío.");
        }

        String rawFilename = multipartFile.getOriginalFilename() != null ? multipartFile.getOriginalFilename() : "image.jpg";
        String originalFilename = StringUtils.cleanPath(rawFilename);

        if (originalFilename.contains("..")) {
            throw new IllegalArgumentException("El nombre del archivo contiene una ruta inválida: " + originalFilename);
        }

        String extension = "";
        int extensionSeparatorIndex = originalFilename.lastIndexOf('.');
        if (extensionSeparatorIndex >= 0) {
            extension = originalFilename.substring(extensionSeparatorIndex).toLowerCase();
        } else {
            extension = ".jpg";
        }

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException(
                    "Tipo de archivo no permitido (" + extension + "). Extensiones permitidas: " + ALLOWED_EXTENSIONS
            );
        }

        String generatedFileName = UUID.randomUUID().toString() + extension;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(generatedFileName);
            Files.copy(multipartFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + generatedFileName;
        } catch (IOException ioException) {
            logger.error("Error storing file: {}", generatedFileName, ioException);
            throw new FileStorageException("Error al almacenar el archivo " + generatedFileName, ioException);
        }
    }

    public boolean deleteFile(String storedFilePathOrUrl) {
        if (storedFilePathOrUrl == null || storedFilePathOrUrl.trim().isEmpty()) {
            return false;
        }

        try {
            String filename = storedFilePathOrUrl;
            if (filename.startsWith("/uploads/")) {
                filename = filename.substring("/uploads/".length());
            } else if (filename.startsWith("uploads/")) {
                filename = filename.substring("uploads/".length());
            }

            Path resolvedFilePath = this.fileStorageLocation.resolve(filename).normalize();
            if (!resolvedFilePath.startsWith(this.fileStorageLocation)) {
                logger.warn("Path traversal attempt detected when deleting file: {}", storedFilePathOrUrl);
                return false;
            }
            return Files.deleteIfExists(resolvedFilePath);
        } catch (IOException ioException) {
            logger.error("Error deleting file: {}", storedFilePathOrUrl, ioException);
            return false;
        }
    }

    public Path getFileStorageLocation() {
        return fileStorageLocation;
    }
}
