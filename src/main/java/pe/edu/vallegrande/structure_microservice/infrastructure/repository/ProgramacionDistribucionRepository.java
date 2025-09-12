package pe.edu.vallegrande.structure_microservice.infrastructure.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.edu.vallegrande.structure_microservice.domain.models.ProgramacionDistribucion;

public interface ProgramacionDistribucionRepository extends ReactiveMongoRepository<ProgramacionDistribucion, String> {
}
