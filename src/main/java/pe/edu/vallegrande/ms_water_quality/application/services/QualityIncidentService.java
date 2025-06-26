package pe.edu.vallegrande.ms_water_quality.application.services;

import pe.edu.vallegrande.ms_water_quality.domain.models.QualityIncident;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.QualityIncidentCreateRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface QualityIncidentService {
    Flux<QualityIncident> getAll();
    Flux<QualityIncident> getResolved();
    Flux<QualityIncident> getUnresolved();
    Mono<QualityIncident> getById(String id);
    Mono<QualityIncident> save(QualityIncidentCreateRequest request);
    Mono<QualityIncident> update(String id, QualityIncident incident);
    Mono<Void> delete(String id);
}
