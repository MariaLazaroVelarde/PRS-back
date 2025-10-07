package pe.edu.vallegrande.ms_water_quality.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.ms_water_quality.application.services.QualityParameterService;
import pe.edu.vallegrande.ms_water_quality.domain.models.QualityParameter;
import pe.edu.vallegrande.ms_water_quality.infrastructure.client.dto.ExternalOrganization;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.QualityParameterCreateRequest;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.response.QualityParameterResponse;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.response.enriched.QualityParameterEnrichedResponse;
import pe.edu.vallegrande.ms_water_quality.infrastructure.exception.CustomException;
import pe.edu.vallegrande.ms_water_quality.infrastructure.repository.QualityParameterRepository;
import pe.edu.vallegrande.ms_water_quality.infrastructure.service.ExternalServiceClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class QualityParameterServiceImpl implements QualityParameterService {

    private final QualityParameterRepository qualityParameterRepository;
    private final ExternalServiceClient externalServiceClient;

    @Override
    public Flux<QualityParameterEnrichedResponse> getAll() {
        return qualityParameterRepository.findAll().flatMap(this::enrichQualityParameter);
    }

    @Override
    public Flux<QualityParameterEnrichedResponse> getAllActive() {
        return qualityParameterRepository.findByStatus("ACTIVE").flatMap(this::enrichQualityParameter);
    }

    @Override
    public Flux<QualityParameterEnrichedResponse> getAllInactive() {
        return qualityParameterRepository.findByStatus("INACTIVE").flatMap(this::enrichQualityParameter);
    }

    @Override
    public Mono<QualityParameterEnrichedResponse> getById(String id) {
        return qualityParameterRepository.findById(id)
                .flatMap(this::enrichQualityParameter)
                .switchIfEmpty(Mono.error(CustomException.notFound("QualityParameter", id)));
    }

    @Override
    public Mono<QualityParameterResponse> save(QualityParameterCreateRequest request) {
        QualityParameter parameter = new QualityParameter();
        // map fields from request
        return qualityParameterRepository.save(parameter).map(p -> new QualityParameterResponse(/* map fields */));
    }

    @Override
    public Mono<QualityParameter> update(String id, QualityParameter updatedParameter) {
        return qualityParameterRepository.findById(id)
                .switchIfEmpty(Mono.error(CustomException.notFound("QualityParameter", id)))
                .flatMap(existing -> qualityParameterRepository.save(updatedParameter));
    }

    @Override
    public Mono<Void> delete(String id) {
        return qualityParameterRepository.deleteById(id);
    }

    @Override
    public Mono<QualityParameterEnrichedResponse> activate(String id) {
        return qualityParameterRepository.findById(id)
                .switchIfEmpty(Mono.error(CustomException.notFound("QualityParameter", id)))
                .flatMap(p -> {
                    p.setStatus("ACTIVE");
                    return qualityParameterRepository.save(p);
                })
                .flatMap(this::enrichQualityParameter);
    }

    @Override
    public Mono<QualityParameterEnrichedResponse> deactivate(String id) {
        return qualityParameterRepository.findById(id)
                .switchIfEmpty(Mono.error(CustomException.notFound("QualityParameter", id)))
                .flatMap(p -> {
                    p.setStatus("INACTIVE");
                    return qualityParameterRepository.save(p);
                })
                .flatMap(this::enrichQualityParameter);
    }

    private Mono<QualityParameterEnrichedResponse> enrichQualityParameter(QualityParameter parameter) {
        Mono<ExternalOrganization> orgMono = externalServiceClient.getOrganizationById(parameter.getOrganizationId())
                .defaultIfEmpty(new ExternalOrganization());

        return orgMono.map(org -> QualityParameterEnrichedResponse.builder()
                .id(parameter.getId())
                .parameterCode(parameter.getParameterCode())
                .parameterName(parameter.getParameterName())
                .unitOfMeasure(parameter.getUnitOfMeasure())
                .minAcceptable(parameter.getMinAcceptable())
                .maxAcceptable(parameter.getMaxAcceptable())
                .optimalRange(parameter.getOptimalRange())
                .testFrequency(parameter.getTestFrequency())
                .status(parameter.getStatus())
                .createdAt(parameter.getCreatedAt())
                .updatedAt(parameter.getUpdatedAt())
                .organization(org)
                .build());
    }
}