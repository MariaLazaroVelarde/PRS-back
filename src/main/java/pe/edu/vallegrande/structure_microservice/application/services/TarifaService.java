package pe.edu.vallegrande.structure_microservice.application.services;

import pe.edu.vallegrande.structure_microservice.domain.models.Tarifa;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.request.TarifaCreateRequest;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.response.TarifaResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TarifaService {
    Flux<Tarifa> getAll();
    Flux<Tarifa> getAllActive();
    Flux<Tarifa> getAllInactive();
    Mono<Tarifa> getById(String id);
    Mono<TarifaResponse> save(TarifaCreateRequest tarifaRequest);
    Mono<Tarifa> update(String id, Tarifa tarifa);
    Mono<Void> delete(String id);
    Mono<Tarifa> activate(String id);
    Mono<Tarifa> deactivate(String id);
}