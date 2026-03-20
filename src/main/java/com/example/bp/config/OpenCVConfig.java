package com.example.bp.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenCVConfig {
    @PostConstruct
    public void init() {
        nu.pattern.OpenCV.loadShared();
    }
}
