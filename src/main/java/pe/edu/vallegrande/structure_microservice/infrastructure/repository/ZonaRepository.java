package pe.edu.vallegrande.structure_microservice.infrastructure.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.structure_microservice.domain.models.Zona;
import reactor.core.publisher.Flux;

@Repository
public interface ZonaRepository extends ReactiveMongoRepository<Zona, String> {
    Flux<Zona> findAllByStatus(String status);
}
