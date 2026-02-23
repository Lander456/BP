package com.example.bp.service.impl;

import com.example.bp.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path root = Paths.get("uploads/iris-scans");

    @PostConstruct
    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch(IOException e) {
            throw new RuntimeException("Could not initialize storage folder!", e);
        }
    }

    @Override
    public String save(@NonNull MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }

            String originalFileName = file.getOriginalFilename();
            String extension = "";

            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }

            String uniqueName = UUID.randomUUID() + extension;

            Files.copy(file.getInputStream(), this.root.resolve(uniqueName), StandardCopyOption.REPLACE_EXISTING);

            return uniqueName;
        } catch(IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public void delete(String fileName) {
        try {
            Path file = root.resolve(fileName);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
