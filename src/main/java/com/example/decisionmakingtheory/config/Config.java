package com.example.decisionmakingtheory.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "my.io")
public class Config {
    private String pathToCSV;
    private String pathToResultFolder;
    private String pathToClothesAlternatives;
    private String pathToClothes;
    private String pathToWeather;
}
