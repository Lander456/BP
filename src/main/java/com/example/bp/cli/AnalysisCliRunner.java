package com.example.bp.cli;

import com.example.bp.analysis.IrisComputerVisionEngine;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Profile("cli")
public class AnalysisCliRunner implements CommandLineRunner {

    private final ApplicationArguments appArgs;
    private final IrisComputerVisionEngine engine;
    //private final IrisImageRepository imageRepository;
    //private final IrisFindingService findingService;

    public AnalysisCliRunner(ApplicationArguments appArgs, IrisComputerVisionEngine engine) {
        this.appArgs = appArgs;
        this.engine = engine;
    }

    @Override
    public void run(String @NonNull ... args) {
        if (!appArgs.containsOption("path")) {
            System.out.println("Error: Please provide a file path using --path=/your/image.jpg");
            return;
        }

        boolean showPreview = appArgs.containsOption("preview");

        String imagePath = appArgs.getOptionValues("path").get(0);
        System.out.println("Attempting to load: " + imagePath);

        if (showPreview) {
            engine.previewImage(imagePath);
        }
    }
}
