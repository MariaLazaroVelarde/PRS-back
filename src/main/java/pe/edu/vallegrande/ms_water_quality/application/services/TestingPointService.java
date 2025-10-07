package pe.edu.vallegrande.ms_water_quality.application.services;

import pe.edu.vallegrande.ms_water_quality.domain.models.TestingPoint;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.TestingPointCreateRequest;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.response.TestingPointResponse;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.response.enriched.TestingPointEnrichedResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TestingPointService {

    Flux<TestingPointEnrichedResponse> getAll();

    Flux<TestingPointEnrichedResponse> getAllActive();

    Flux<TestingPointEnrichedResponse> getAllInactive();

    Mono<TestingPointEnrichedResponse> getById(String id);

    Mono<TestingPointResponse> save(TestingPointCreateRequest request);

    Mono<TestingPoint> update(String id, TestingPoint point);

    Mono<Void> delete(String id);

    Mono<TestingPointEnrichedResponse> activate(String id);

    Mono<TestingPointEnrichedResponse> deactivate(String id);
}
