package com.example.bp.service;

import jakarta.annotation.Nonnull;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    void init();
    String save(MultipartFile file);
    void delete(String fileName);
}
