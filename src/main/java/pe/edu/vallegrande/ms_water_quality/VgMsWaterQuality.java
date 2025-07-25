package pe.edu.vallegrande.ms_water_quality;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@SpringBootApplication
@OpenAPIDefinition
public class VgMsWaterQuality {

    public static void main(String[] args) {
        SpringApplication.run(VgMsWaterQuality.class, args);
    }
}