package pe.edu.vallegrande.structure_microservice.application.services;

import pe.edu.vallegrande.structure_microservice.domain.models.Calle;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.request.CalleCreateRequest;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.response.CalleResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CalleService {
    Flux<Calle> getAll();
    Flux<Calle> getAllActive();
    Flux<Calle> getAllInactive();
    Mono<Calle> getById(String id);
    Mono<CalleResponse> save(CalleCreateRequest calleRequest);
    Mono<Calle> update(String id, Calle calle);
    Mono<Void> delete(String id);
    Mono<Calle> activate(String id);
    Mono<Calle> deactivate(String id);
}
