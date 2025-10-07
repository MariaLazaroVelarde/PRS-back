package pe.edu.vallegrande.ms_water_quality;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@OpenAPIDefinition
@Slf4j
public class VgMsWaterQuality {

    public static void main(String[] args) {
        SpringApplication.run(VgMsWaterQuality.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(ApplicationReadyEvent event) {
        Environment environment = event.getApplicationContext().getEnvironment();
        String port = environment.getProperty("server.port", "8087");
        String appName = environment.getProperty("spring.application.name", "ms-water-quality");
        String userServiceUrl = environment.getProperty("user-service.base-url", "Not configured");
        
        log.info("================================================================================");
        log.info("🚀 {} STARTED SUCCESSFULLY", appName.toUpperCase());
        log.info("🌐 Server running on port: {}", port);
        log.info("� UAPI Base URL: http://localhost:{}/api/v2/admin", port);
        log.info("� Uselr Service URL: {}", userServiceUrl);
        log.info("📊 Health Check: http://localhost:{}/actuator/health", port);
        log.info("================================================================================");
    }
}