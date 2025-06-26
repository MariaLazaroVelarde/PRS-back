package pe.edu.vallegrande.ms_water_quality.application.services;

import pe.edu.vallegrande.ms_water_quality.domain.models.TestingPoint;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.TestingPointCreateRequest;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.response.TestingPointResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TestingPointService {

    Flux<TestingPoint> getAll();

    Flux<TestingPoint> getAllActive();

    Flux<TestingPoint> getAllInactive();

    Mono<TestingPoint> getById(String id);

    Mono<TestingPointResponse> save(TestingPointCreateRequest request);

    Mono<TestingPoint> update(String id, TestingPoint point);

    Mono<Void> delete(String id);

    Mono<TestingPoint> activate(String id);

    Mono<TestingPoint> deactivate(String id);
}
