package com.example.decisionmakingtheory.lab1.services.implementation;

import com.example.decisionmakingtheory.lab1.config.Config;
import com.example.decisionmakingtheory.lab1.services.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileResultService implements ResultService {
    private final FileServiceImpl fileService;
    private final Config config;

    @Override
    public Resource downloadScatter(String fileName) {
        return fileService.loadAsResource(config.getPathToResultFolder() + "/" + fileName);
    }
}
