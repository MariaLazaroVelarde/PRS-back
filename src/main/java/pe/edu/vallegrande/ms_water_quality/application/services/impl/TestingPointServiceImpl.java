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

    @Override
    public Flux<TestingPoint> getAll() {
        return repository.findAll()
                .doOnNext(tp -> log.info("Testing Point retrieved: {}", tp));
    }

    @Override
    public Flux<TestingPoint> getAllActive() {
        return repository.findAllByStatus(Constants.ACTIVE.name());
    }

    @Override
    public Flux<TestingPoint> getAllInactive() {
        return repository.findAllByStatus(Constants.INACTIVE.name());
    }

    @Override
    public Mono<TestingPoint> getById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Testing Point not found",
                        "The requested point with id " + id + " was not found")));
    }

   @Override
public Mono<TestingPointResponse> save(TestingPointCreateRequest request) {
    TestingPoint point = new TestingPoint();
    point.setOrganizationId(request.getOrganizationId());
    point.setPointCode(request.getPointCode());
    point.setPointName(request.getPointName());
    point.setPointType(request.getPointType());
    point.setZoneId(request.getZoneId());
    point.setLocationDescription(request.getLocationDescription());
    point.setCoordinates(new TestingPoint.Coordinates(
    request.getCoordinates().getLatitude(),
    request.getCoordinates().getLongitude()
));

    point.setStatus(Constants.ACTIVE.name());
    point.setCreatedAt(LocalDateTime.now());

    return repository.save(point)
            .map(saved -> {
                TestingPointResponse response = new TestingPointResponse();
                response.setId(saved.getId());
                response.setOrganizationId(saved.getOrganizationId());
                response.setPointCode(saved.getPointCode());
                response.setPointName(saved.getPointName());
                response.setPointType(saved.getPointType());
                response.setZoneId(saved.getZoneId());
                response.setLocationDescription(saved.getLocationDescription());

                if (saved.getCoordinates() != null) {
                    response.setCoordinates(new TestingPointResponse.Coordinates(
                            saved.getCoordinates().getLatitude(),
                            saved.getCoordinates().getLongitude()
                    ));
                }

                response.setStatus(saved.getStatus());
                response.setCreatedAt(saved.getCreatedAt());
                return response;
            });
}

    @Override
    public Mono<TestingPoint> update(String id, TestingPoint updatedPoint) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Testing Point not found",
                        "Cannot update non-existent testing point with id " + id)))
                .flatMap(existing -> {
                    existing.setPointCode(updatedPoint.getPointCode());
                    existing.setPointName(updatedPoint.getPointName());
                    existing.setPointType(updatedPoint.getPointType());
                    existing.setZoneId(updatedPoint.getZoneId());
                    existing.setLocationDescription(updatedPoint.getLocationDescription());
                    existing.setCoordinates(updatedPoint.getCoordinates());
                    existing.setCreatedAt(LocalDateTime.now());//SALE ERROR setUpdatedAt
                    return repository.save(existing);
                });
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Testing Point not found",
                        "Cannot delete non-existent testing point with id " + id)))
                .flatMap(repository::delete);
    }

    @Override
    public Mono<TestingPoint> activate(String id) {
        return changeStatus(id, Constants.ACTIVE.name());
    }

    @Override
    public Mono<TestingPoint> deactivate(String id) {
        return changeStatus(id, Constants.INACTIVE.name());
    }

    private Mono<TestingPoint> changeStatus(String id, String status) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Testing Point not found",
                        "Cannot change status of non-existent point with id " + id)))
                .flatMap(point -> {
                    point.setStatus(status);
                    point.setCreatedAt(LocalDateTime.now()); //SALE ERROR EN setUpdatedAt
                    return repository.save(point);
                });
    }
}
