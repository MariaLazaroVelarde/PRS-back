package pe.edu.vallegrande.ms_water_quality.application.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class QualityParameterServiceImpl implements QualityParameterService {

    @Autowired
    private QualityParameterRepository repository;

    @Override
    public Flux<QualityParameter> getAll() {
        return repository.findAll()
                .doOnNext(param -> log.info("QualityParameter retrieved: {}", param));
    }

    @Override
    public Flux<QualityParameter> getAllActive() {
        return repository.findAllByStatus(Constants.ACTIVE.name());
    }

    @Override
    public Flux<QualityParameter> getAllInactive() {
        return repository.findAllByStatus(Constants.INACTIVE.name());
    }

    @Override
    public Mono<QualityParameter> getById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Quality Parameter not found",
                        "No parameter with id " + id + " was found")));
    }

    @Override
    public Mono<QualityParameterResponse> save(QualityParameterCreateRequest request) { 
        QualityParameter param = new QualityParameter();
        param.setOrganizationId(request.getOrganizationId());
        param.setParameterCode(request.getParameterCode());
        param.setParameterName(request.getParameterName());
        param.setUnitOfMeasure(request.getUnitOfMeasure());
        param.setMinAcceptable(request.getMinAcceptable());
        param.setMaxAcceptable(request.getMaxAcceptable());
       param.setOptimalRange(
    new QualityParameter.OptimalRange(
        request.getOptimalRange().getMin(),
        request.getOptimalRange().getMax()
    )
);
        param.setTestFrequency(request.getTestFrequency());
        param.setStatus(Constants.ACTIVE.name());
        param.setCreatedAt(LocalDateTime.now());

        return repository.save(param)
                .map(saved -> {
                    QualityParameterResponse response = new QualityParameterResponse(); //Error QualityParameterResponse
                    response.setId(saved.getId());
                    response.setParameterName(saved.getParameterName());
                    response.setParameterCode(saved.getParameterCode());
                    response.setTestFrequency(saved.getTestFrequency());
                    response.setStatus(saved.getStatus());
                    response.setCreatedAt(saved.getCreatedAt());
                    return response;
                });
    }

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

    @Override
    public Mono<Void> delete(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Quality Parameter not found",
                        "Cannot delete non-existent parameter with id " + id)))
                .flatMap(repository::delete);
    }

    @Override
    public Mono<QualityParameter> activate(String id) {
        return changeStatus(id, Constants.ACTIVE.name());
    }

    @Override
    public Mono<QualityParameter> deactivate(String id) {
        return changeStatus(id, Constants.INACTIVE.name());
    }

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
}
