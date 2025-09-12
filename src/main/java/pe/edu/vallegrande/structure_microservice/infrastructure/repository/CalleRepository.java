package pe.edu.vallegrande.structure_microservice.infrastructure.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.structure_microservice.domain.models.Calle;
import reactor.core.publisher.Flux;

@Repository
public interface CalleRepository extends ReactiveMongoRepository<Calle, String> {
    Flux<Calle> findAllByEstado(boolean estado);  // Corrección aquí
    Flux<Calle> findAllByZonaId(String zonaId);    // Para obtener calles por zona
}
