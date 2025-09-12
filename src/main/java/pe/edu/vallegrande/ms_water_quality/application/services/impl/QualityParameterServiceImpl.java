package pe.edu.vallegrande.ms_water_quality.application.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.ms_water_quality.application.services.QualityParameterService;
import pe.edu.vallegrande.ms_water_quality.domain.enums.Constants;
import pe.edu.vallegrande.ms_water_quality.domain.models.QualityParameter;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.QualityParameterCreateRequest;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.response.QualityParameterResponse;
import pe.edu.vallegrande.ms_water_quality.infrastructure.exception.CustomException;
import pe.edu.vallegrande.ms_water_quality.infrastructure.repository.QualityParameterRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class QualityParameterServiceImpl implements QualityParameterService {

    private final QualityParameterRepository repository;

    /** GET /api/admin/quality/parameters */
    @Override
    public Flux<QualityParameter> getAll() {
        return repository.findAll()
                .doOnNext(param -> log.info("QualityParameter retrieved: {}", param));
    }

    /** GET /api/admin/quality/parameters/active */
    @Override
    public Flux<QualityParameter> getAllActive() {
        return repository.findAllByStatus(Constants.ACTIVE.name());
    }

    /** GET /api/admin/quality/parameters/inactive */
    @Override
    public Flux<QualityParameter> getAllInactive() {
        return repository.findAllByStatus(Constants.INACTIVE.name());
    }

    /** GET /api/admin/quality/parameters/{id} */
    @Override
    public Mono<QualityParameter> getById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Quality Parameter not found",
                        "No parameter with id " + id + " was found")));
    }

    /** POST /api/admin/quality/parameters */
    @Override
    public Mono<QualityParameterResponse> save(QualityParameterCreateRequest request) {
        if (request == null || request.getParameterName() == null || request.getOrganizationId() == null) {
            return Mono.error(new CustomException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Invalid request",
                    "Organization ID and Parameter Name are required."
            ));
        }

        return generateNextCode()
                .flatMap(code -> {
                    QualityParameter param = new QualityParameter();
                    param.setOrganizationId(request.getOrganizationId());
                    param.setParameterCode(code); // Se genera automáticamente
                    param.setParameterName(request.getParameterName());
                    param.setUnitOfMeasure(request.getUnitOfMeasure());
                    param.setMinAcceptable(request.getMinAcceptable());
                    param.setMaxAcceptable(request.getMaxAcceptable());
                    param.setOptimalRange(new QualityParameter.OptimalRange(
                            request.getOptimalRange().getMin(),
                            request.getOptimalRange().getMax()));
                    param.setTestFrequency(request.getTestFrequency());
                    param.setStatus(Constants.ACTIVE.name());
                    param.setCreatedAt(LocalDateTime.now());

                    return repository.save(param)
                            .map(saved -> {
                                QualityParameterResponse response = new QualityParameterResponse();
                                response.setId(saved.getId());
                                response.setParameterName(saved.getParameterName());
                                response.setParameterCode(saved.getParameterCode());
                                response.setTestFrequency(saved.getTestFrequency());
                                response.setStatus(saved.getStatus());
                                response.setCreatedAt(saved.getCreatedAt());
                                return response;
                            });
                });
    }

    /** PUT /api/admin/quality/parameters/{id} */
    @Override
    public Mono<QualityParameter> update(String id, QualityParameter updatedParam) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Quality Parameter not found",
                        "Cannot update non-existent parameter with id " + id)))
                .flatMap(existing -> {
                    existing.setParameterName(updatedParam.getParameterName());
                    existing.setParameterCode(updatedParam.getParameterCode());
                    existing.setUnitOfMeasure(updatedParam.getUnitOfMeasure());
                    existing.setMinAcceptable(updatedParam.getMinAcceptable());
                    existing.setMaxAcceptable(updatedParam.getMaxAcceptable());
                    existing.setOptimalRange(updatedParam.getOptimalRange());
                    existing.setTestFrequency(updatedParam.getTestFrequency());
                    existing.setUpdatedAt(LocalDateTime.now());

                    return repository.save(existing);
                });
    }

    /** DELETE /api/admin/quality/parameters/{id} */
    @Override
    public Mono<Void> delete(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Quality Parameter not found",
                        "Cannot delete non-existent parameter with id " + id)))
                .flatMap(repository::delete);
    }

    /** PATCH /api/admin/quality/parameters/{id}/activate */
    @Override
    public Mono<QualityParameter> activate(String id) {
        return changeStatus(id, Constants.ACTIVE.name());
    }

    /** PATCH /api/admin/quality/parameters/{id}/deactivate */
    @Override
    public Mono<QualityParameter> deactivate(String id) {
        return changeStatus(id, Constants.INACTIVE.name());
    }

    /** Cambia el estado de activo/inactivo */
    private Mono<QualityParameter> changeStatus(String id, String status) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Quality Parameter not found",
                        "Cannot change status of non-existent parameter with id " + id)))
                .flatMap(param -> {
                    param.setStatus(status);
                    param.setUpdatedAt(LocalDateTime.now());
                    return repository.save(param);
                });
    }

    /** Genera código incremental tipo PRM### */
    private Mono<String> generateNextCode() {
        return repository.findAll()
                .filter(p -> p.getParameterCode() != null && p.getParameterCode().startsWith("PRM"))
                .sort((p1, p2) -> p2.getParameterCode().compareTo(p1.getParameterCode()))
                .next()
                .map(last -> {
                    try {
                        int number = Integer.parseInt(last.getParameterCode().replace("PRM", ""));
                        return String.format("PRM%03d", number + 1);
                    } catch (Exception e) {
                        return "PRM001";
                    }
                })
                .defaultIfEmpty("PRM001");
    }
}
