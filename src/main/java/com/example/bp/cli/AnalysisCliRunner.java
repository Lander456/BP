package com.example.bp.cli;

import com.example.bp.analysis.IrisComputerVisionEngine;
import com.example.bp.dal.repository.IrisImageRepository;
import com.example.bp.service.IrisFindingService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("cli")
@RequiredArgsConstructor
public class AnalysisCliRunner implements CommandLineRunner {

    private final IrisComputerVisionEngine engine;
    private final IrisImageRepository imageRepository;
    private final IrisFindingService findingService;

    @Override
    public void run(String... args) {

    }
}
