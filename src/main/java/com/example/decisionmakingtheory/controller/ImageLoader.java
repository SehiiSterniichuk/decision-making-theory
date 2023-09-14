package com.example.decisionmakingtheory.controller;

import com.example.decisionmakingtheory.services.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/lab1/results")
@RequiredArgsConstructor
public class ImageLoader {
    private final ResultService resultService;

    @GetMapping("/download/scatter/{fileName}")
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        Resource file = resultService.downloadScatter(fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
