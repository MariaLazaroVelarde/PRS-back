package pe.edu.vallegrande.structure_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@SpringBootApplication
@OpenAPIDefinition
public class StructureMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StructureMicroserviceApplication.class, args);
    }
}