package com.osvaldozunniga.apiSpringBoot.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class FileStorageServiceTest {

    private FileStorageService fileStorageService;
    private Path temporaryDirectory;

    @BeforeEach
    void setUp() throws Exception {
        temporaryDirectory = Files.createTempDirectory("test-uploads");
        fileStorageService = new FileStorageService(temporaryDirectory.toString());
    }

    @AfterEach
    void tearDown() {
        File directory = temporaryDirectory.toFile();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        directory.delete();
    }

    @Test
    void testStoreFile_Success() {
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "cow.jpg",
                "image/jpeg",
                "dummy image content".getBytes()
        );

        String resultPath = fileStorageService.storeFile(file);

        assertNotNull(resultPath);
        assertTrue(resultPath.startsWith("/uploads/"));
        assertTrue(resultPath.endsWith(".jpg"));

        String filename = resultPath.substring("/uploads/".length());
        assertTrue(Files.exists(temporaryDirectory.resolve(filename)));
    }

    @Test
    void testStoreFile_InvalidExtension() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "script.sh",
                "text/plain",
                "echo 'hello'".getBytes()
        );

        assertThrows(IllegalArgumentException.class, () -> {
            fileStorageService.storeFile(file);
        });
    }

    @Test
    void testStoreFile_EmptyFile() {
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "empty.jpg",
                "image/jpeg",
                new byte[0]
        );

        assertThrows(IllegalArgumentException.class, () -> {
            fileStorageService.storeFile(file);
        });
    }

    @Test
    void testDeleteFile() {
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "cow.png",
                "image/png",
                "dummy png content".getBytes()
        );

        String storedPath = fileStorageService.storeFile(file);
        String filename = storedPath.substring("/uploads/".length());
        assertTrue(Files.exists(temporaryDirectory.resolve(filename)));

        boolean isDeleted = fileStorageService.deleteFile(storedPath);
        assertTrue(isDeleted);
        assertFalse(Files.exists(temporaryDirectory.resolve(filename)));
    }

    @Test
    void testStoreFile_PathTraversalRejected() {
        MockMultipartFile fileWithTraversal = new MockMultipartFile(
                "image",
                "../../malicious.jpg",
                "image/jpeg",
                "image data".getBytes()
        );

        assertThrows(IllegalArgumentException.class, () -> {
            fileStorageService.storeFile(fileWithTraversal);
        });
    }

    @Test
    void testDeleteFile_PathTraversalPrevented() throws Exception {
        Path externalSensitiveFile = temporaryDirectory.getParent().resolve("external-sensitive-file.txt");
        Files.write(externalSensitiveFile, "sensitive file content".getBytes());

        boolean isDeleted = fileStorageService.deleteFile("/uploads/../external-sensitive-file.txt");

        assertFalse(isDeleted);
        assertTrue(Files.exists(externalSensitiveFile));

        Files.deleteIfExists(externalSensitiveFile);
    }
}
