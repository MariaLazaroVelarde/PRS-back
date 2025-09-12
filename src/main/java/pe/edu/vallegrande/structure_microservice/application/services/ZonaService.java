package pe.edu.vallegrande.structure_microservice.application.services;

import pe.edu.vallegrande.structure_microservice.domain.models.Zona;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.request.ZonaCreateRequest;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.response.ZonaResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ZonaService {
    Flux<Zona> getAll();
    Flux<Zona> getAllActive();
    Flux<Zona> getAllInactive();
    Mono<Zona> getById(String id);
    Mono<ZonaResponse> save(ZonaCreateRequest zonaRequest);
    Mono<Zona> update(String id, Zona zona);
    Mono<Void> delete(String id);
    Mono<Zona> activate(String id);
    Mono<Zona> deactivate(String id);
}
