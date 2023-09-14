package com.example.decisionmakingtheory.services.implementation;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@AllArgsConstructor
@Slf4j
public class FileServiceImpl {

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
}