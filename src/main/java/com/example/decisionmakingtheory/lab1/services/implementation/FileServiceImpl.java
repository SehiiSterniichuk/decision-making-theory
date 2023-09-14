package com.example.decisionmakingtheory.lab1.services.implementation;


import com.example.decisionmakingtheory.lab1.config.Config;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class FileServiceImpl {
    private final Config config;

    public Resource loadAsResource(String path) {
        File file = new File(path);
        Resource resource = new FileSystemResource(file);
        if (resource.exists()) {
            return resource;
        }
        String message = "file: " + path
                + " doesn't exist or file is not readable";
        throw new IllegalArgumentException(message);
    }

    public void deleteOldOutput() {
        var pathToResultFolder = Path.of(config.getPathToResultFolder());
        try {
            deleteDirectory(pathToResultFolder);
            Files.createDirectory(Path.of(config.getPathToResultFolder()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteDirectory(Path directory) throws IOException {
        if (Files.exists(directory)) {
            try (Stream<Path> walk = Files.walk(directory)) {
                walk.sorted((a, b) -> -a.compareTo(b)) // Reverse order for files
                        .forEach(file -> {
                            try {
                                Files.delete(file);
                            } catch (IOException e) {
                                log.error(e.getMessage());
                            }
                        });
            }
        } else {
            log.info("Directory does not exist: " + directory);
        }
    }
}