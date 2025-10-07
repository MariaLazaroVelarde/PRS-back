package pe.edu.vallegrande.ms_water_quality.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.ms_water_quality.application.services.TestingPointService;
import pe.edu.vallegrande.ms_water_quality.domain.models.TestingPoint;
import pe.edu.vallegrande.ms_water_quality.infrastructure.client.dto.ExternalOrganization;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.TestingPointCreateRequest;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.response.TestingPointResponse;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.response.enriched.TestingPointEnrichedResponse;
import pe.edu.vallegrande.ms_water_quality.infrastructure.exception.CustomException;
import pe.edu.vallegrande.ms_water_quality.infrastructure.repository.TestingPointRepository;
import pe.edu.vallegrande.ms_water_quality.infrastructure.service.ExternalServiceClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TestingPointServiceImpl implements TestingPointService {

    private final TestingPointRepository testingPointRepository;
    private final ExternalServiceClient externalServiceClient;

    @Override
    public Flux<TestingPointEnrichedResponse> getAll() {
        return testingPointRepository.findAll().flatMap(this::enrichTestingPoint);
    }

    @Override
    public Flux<TestingPointEnrichedResponse> getAllActive() {
        return testingPointRepository.findByStatus("ACTIVE").flatMap(this::enrichTestingPoint);
    }

    @Override
    public Flux<TestingPointEnrichedResponse> getAllInactive() {
        return testingPointRepository.findByStatus("INACTIVE").flatMap(this::enrichTestingPoint);
    }

    @Override
    public Mono<TestingPointEnrichedResponse> getById(String id) {
        return testingPointRepository.findById(id)
                .flatMap(this::enrichTestingPoint)
                .switchIfEmpty(Mono.error(CustomException.notFound("TestingPoint", id)));
    }

    @Override
    public Mono<TestingPointResponse> save(TestingPointCreateRequest request) {
        TestingPoint testingPoint = new TestingPoint();
        // Mapping from request to model
        testingPoint.setOrganizationId(request.getOrganizationId());
        testingPoint.setPointCode(request.getPointCode());
        testingPoint.setPointName(request.getPointName());
        testingPoint.setPointType(request.getPointType());
        testingPoint.setZoneId(request.getZoneId());
        testingPoint.setLocationDescription(request.getLocationDescription());
        testingPoint.setStreet(request.getStreet());
        testingPoint.setCoordinates(new TestingPoint.Coordinates(request.getCoordinates().getLatitude(), request.getCoordinates().getLongitude()));
        testingPoint.setCreatedAt(LocalDateTime.now());

        return testingPointRepository.save(testingPoint).map(saved -> new TestingPointResponse(/* map fields */));
    }

    @Override
    public Mono<TestingPoint> update(String id, TestingPoint point) {
        return testingPointRepository.findById(id)
                .switchIfEmpty(Mono.error(CustomException.notFound("TestingPoint", id)))
                .flatMap(existing -> {
                    existing.setOrganizationId(point.getOrganizationId());
                    // map other fields
                    return testingPointRepository.save(existing);
                });
    }

    @Override
    public Mono<Void> delete(String id) {
        return testingPointRepository.deleteById(id);
    }

    @Override
    public Mono<TestingPointEnrichedResponse> activate(String id) {
        return testingPointRepository.findById(id)
                .switchIfEmpty(Mono.error(CustomException.notFound("TestingPoint", id)))
                .flatMap(point -> {
                    point.setStatus("ACTIVE");
                    return testingPointRepository.save(point);
                })
                .flatMap(this::enrichTestingPoint);
    }

    @Override
    public Mono<TestingPointEnrichedResponse> deactivate(String id) {
        return testingPointRepository.findById(id)
                .switchIfEmpty(Mono.error(CustomException.notFound("TestingPoint", id)))
                .flatMap(point -> {
                    point.setStatus("INACTIVE");
                    return testingPointRepository.save(point);
                })
                .flatMap(this::enrichTestingPoint);
    }

    private Mono<TestingPointEnrichedResponse> enrichTestingPoint(TestingPoint point) {
        Mono<ExternalOrganization> orgMono = externalServiceClient.getOrganizationById(point.getOrganizationId())
                .defaultIfEmpty(new ExternalOrganization()); // Evita error si no se encuentra la organización

        return orgMono.map(org -> TestingPointEnrichedResponse.builder()
                .id(point.getId())
                .pointCode(point.getPointCode())
                .pointName(point.getPointName())
                .pointType(point.getPointType())
                .zoneId(point.getZoneId())
                .locationDescription(point.getLocationDescription())
                .street(point.getStreet())
                .coordinates(point.getCoordinates())
                .status(point.getStatus())
                .createdAt(point.getCreatedAt())
                .updatedAt(point.getUpdatedAt())
                .organization(org)
                .build());
    }
}