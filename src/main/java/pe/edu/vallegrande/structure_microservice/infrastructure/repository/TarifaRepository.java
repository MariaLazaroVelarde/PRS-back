package pe.edu.vallegrande.structure_microservice.infrastructure.repository;

import pe.edu.vallegrande.structure_microservice.domain.models.Tarifa;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TarifaRepository extends ReactiveMongoRepository<Tarifa, String> {
    // Método para encontrar tarifas por zonaId
    Flux<Tarifa> findAllByZonaId(String zonaId);

    // Método para encontrar tarifas activas
    Flux<Tarifa> findAllByStatus(String status);

    // Método para verificar si existe una tarifa con un ID específico
    Mono<Boolean> existsByZonaId(String zonaId);
}