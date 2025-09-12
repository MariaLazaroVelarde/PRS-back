package pe.edu.vallegrande.structure_microservice.infrastructure.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.edu.vallegrande.structure_microservice.domain.models.IncidenciaDistribucion;

public interface IncidenciaDistribucionRepository extends ReactiveMongoRepository<IncidenciaDistribucion, String> {
}
