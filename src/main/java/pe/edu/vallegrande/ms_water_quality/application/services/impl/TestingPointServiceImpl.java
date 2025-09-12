package pe.edu.vallegrande.ms_water_quality.application.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.ms_water_quality.application.services.TestingPointService;
import pe.edu.vallegrande.ms_water_quality.domain.enums.Constants;
import pe.edu.vallegrande.ms_water_quality.domain.models.TestingPoint;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.TestingPointCreateRequest;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.response.TestingPointResponse;
import pe.edu.vallegrande.ms_water_quality.infrastructure.exception.CustomException;
import pe.edu.vallegrande.ms_water_quality.infrastructure.repository.TestingPointRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestingPointServiceImpl implements TestingPointService {

    private final TestingPointRepository repository;

    /** GET /sampling-points */
    @Override
    public Flux<TestingPoint> getAll() {
        return repository.findAll()
                .doOnNext(tp -> log.info("Retrieved point: {}", tp.getId()));
    }

    /** GET /sampling-points/active */
    @Override
    public Flux<TestingPoint> getAllActive() {
        return repository.findAllByStatus(Constants.ACTIVE.name())
                .doOnNext(tp -> log.info("Active point: {}", tp.getId()));
    }

    /** GET /sampling-points/inactive */
    @Override
    public Flux<TestingPoint> getAllInactive() {
        return repository.findAllByStatus(Constants.INACTIVE.name())
                .doOnNext(tp -> log.info("Inactive point: {}", tp.getId()));
    }

    /** GET /sampling-points/{id} */
    @Override
    public Mono<TestingPoint> getById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Testing Point not found",
                        "The requested point with id " + id + " was not found")));
    }

    /** POST /sampling-points */
    @Override
    public Mono<TestingPointResponse> save(TestingPointCreateRequest request) {
        if (request == null || request.getOrganizationId() == null || request.getPointName() == null) {
            return Mono.error(new CustomException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Invalid request",
                    "Organization ID and Point Name are required."));
        }

        return generateNextCode()
                .flatMap(nextCode -> {
                    log.info("Generated next code: {}", nextCode);
                    TestingPoint point = new TestingPoint();
                    point.setOrganizationId(request.getOrganizationId());
                    point.setPointCode(nextCode);
                    point.setPointName(request.getPointName());
                    point.setPointType(request.getPointType());
                    point.setZoneId(request.getZoneId());
                    point.setLocationDescription(request.getLocationDescription());

                    if (request.getCoordinates() != null) {
                        point.setCoordinates(new TestingPoint.Coordinates(
                                request.getCoordinates().getLatitude(),
                                request.getCoordinates().getLongitude()));
                    }

                    point.setStatus(Constants.ACTIVE.name());
                    point.setCreatedAt(LocalDateTime.now());
                    point.setUpdatedAt(LocalDateTime.now());

                    return repository.save(point)
                            .doOnSuccess(saved -> log.info("Saved point: {}", saved.getId()))
                            .map(this::mapToResponse);
                });
    }

    /** PUT /sampling-points/{id} */
    @Override
    public Mono<TestingPoint> update(String id, TestingPoint updatedPoint) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Testing Point not found",
                        "Cannot update non-existent point with id " + id)))
                .flatMap(existing -> generateNextCode()
                        .flatMap(newCode -> {
                            existing.setPointCode(newCode); // Código nuevo en cada actualización
                            existing.setPointName(updatedPoint.getPointName());
                            existing.setPointType(updatedPoint.getPointType());
                            existing.setZoneId(updatedPoint.getZoneId());
                            existing.setLocationDescription(updatedPoint.getLocationDescription());
                            existing.setCoordinates(updatedPoint.getCoordinates());
                            existing.setUpdatedAt(LocalDateTime.now());

                            log.info("Updated point {} with new code {}", id, newCode);
                            return repository.save(existing);
                        }));
    }

    /** DELETE /sampling-points/{id} */
    @Override
    public Mono<Void> delete(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Testing Point not found",
                        "Cannot delete non-existent point with id " + id)))
                .flatMap(repository::delete);
    }

    /** PATCH /sampling-points/{id}/activate */
    @Override
    public Mono<TestingPoint> activate(String id) {
        return changeStatus(id, Constants.ACTIVE.name());
    }

    /** PATCH /sampling-points/{id}/deactivate */
    @Override
    public Mono<TestingPoint> deactivate(String id) {
        return changeStatus(id, Constants.INACTIVE.name());
    }

    // Método interno para activar/desactivar
    private Mono<TestingPoint> changeStatus(String id, String status) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Testing Point not found",
                        "Cannot change status of non-existent point with id " + id)))
                .flatMap(point -> {
                    point.setStatus(status);
                    point.setUpdatedAt(LocalDateTime.now());
                    log.info("Changed status of {} to {}", id, status);
                    return repository.save(point);
                });
    }

    // Generar siguiente código PM###
    private Mono<String> generateNextCode() {
        return repository.findAll()
                .filter(p -> p.getPointCode() != null && !p.getPointCode().isBlank()
                        && p.getPointCode().startsWith("PM"))
                .sort((p1, p2) -> p2.getPointCode().compareTo(p1.getPointCode()))
                .next()
                .map(last -> {
                    try {
                        int number = Integer.parseInt(last.getPointCode().replace("PM", ""));
                        return String.format("PM%03d", number + 1);
                    } catch (Exception e) {
                        return "PM001";
                    }
                })
                .defaultIfEmpty("PM001");
    }

    // Convertir modelo a DTO de respuesta
    private TestingPointResponse mapToResponse(TestingPoint point) {
        TestingPointResponse response = new TestingPointResponse();
        response.setId(point.getId());
        response.setOrganizationId(point.getOrganizationId());
        response.setPointCode(point.getPointCode());
        response.setPointName(point.getPointName());
        response.setPointType(point.getPointType());
        response.setZoneId(point.getZoneId());
        response.setLocationDescription(point.getLocationDescription());

        if (point.getCoordinates() != null) {
            response.setCoordinates(new TestingPointResponse.Coordinates(
                    point.getCoordinates().getLatitude(),
                    point.getCoordinates().getLongitude()));
        }

        response.setStatus(point.getStatus());
        response.setCreatedAt(point.getCreatedAt());
        return response;
    }
}
