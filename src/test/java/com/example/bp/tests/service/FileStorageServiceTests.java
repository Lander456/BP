package com.example.bp.tests.service;

import com.example.bp.service.FileStorageService;
import com.example.bp.service.impl.FileStorageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileStorageServiceTests {

    @InjectMocks
    private FileStorageServiceImpl fileStorageService;

    @Mock
    private MultipartFile multipartFile;

    @Test
    void initCreatesDirectory() {
        try(MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.createDirectories(any(Path.class))).thenReturn(null);

            assertDoesNotThrow(() -> fileStorageService.init());

            mockedFiles.verify(() -> Files.createDirectories(any(Path.class)));
        }
    }

    @Test
    void saveStoresFile() throws Exception {
        UUID fakeUUID = UUID.randomUUID();
        String originalFileName = "iris-scan-left.jpg";
        String expectedFileName = fakeUUID + ".jpg";
        InputStream inputStream = new ByteArrayInputStream("dummyImgData".getBytes());

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn(originalFileName);
        when(multipartFile.getInputStream()).thenReturn(inputStream);

        try (MockedStatic<UUID> mockedUuid = mockStatic(UUID.class, Mockito.CALLS_REAL_METHODS  );
            MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {

            mockedUuid.when(UUID::randomUUID).thenReturn(fakeUUID);
            mockedFiles.when(() -> Files.copy(any(InputStream.class), any(Path.class), any(StandardCopyOption.class)))
                    .thenReturn(1L);

            String result = fileStorageService.save(multipartFile);

            assertEquals(expectedFileName, result);
            mockedFiles.verify(() -> Files.copy(
                    eq(inputStream),
                    any(Path.class),
                    eq(StandardCopyOption.REPLACE_EXISTING)
            ));
        }
    }

    @Test
    void saveThrowsExceptionWhenFileIsEmpty() {
        when(multipartFile.isEmpty()).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> fileStorageService.save(multipartFile));
        assertEquals("Failed to store empty file.", exception.getMessage());
    }

    @Test
    void saveThrowsExceptionOnIoException() throws Exception {
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("iris-scan-left.jpg");
        when(multipartFile.getInputStream()).thenThrow(new IOException("Stream failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> fileStorageService.save(multipartFile));
        assertTrue(exception.getMessage().contains("Could not store the file. Error: Stream failed"));
    }

    @Test
    void deleteRemovesFile() {
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.deleteIfExists(any(Path.class))).thenReturn(true);

            assertDoesNotThrow(() -> fileStorageService.delete("someFile.jpg"));

            mockedFiles.verify(() -> Files.deleteIfExists(any(Path.class)));
        }
    }

    @Test
    void deleteThrowsExceptionOnIoException() {
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.deleteIfExists(any(Path.class)))
                    .thenThrow(new IOException("File locked"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> fileStorageService.delete("someFile.jpg"));
            assertTrue(exception.getMessage().contains("Error: File locked"));
        }
    }
}
