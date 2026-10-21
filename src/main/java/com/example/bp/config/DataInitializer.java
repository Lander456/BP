package com.example.bp.config;

import com.example.bp.api.dto.IridologistCreateDto;
import com.example.bp.dal.repository.IridologistRepository;
import com.example.bp.service.IridologistService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(IridologistRepository repository, IridologistService service) {
        return args -> {
            if (!repository.existsByUsername("jdoe")) {
                service.register(new IridologistCreateDto(
                        "jdoe",
                        "John",
                        "Doe",
                        "Password123!"
                ));
                System.out.println(">>> Seeded default iridologist: jdoe / Password123!");
            }
        };
    }
}
