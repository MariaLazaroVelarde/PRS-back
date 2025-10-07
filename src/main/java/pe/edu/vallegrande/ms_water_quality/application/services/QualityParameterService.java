package pe.edu.vallegrande.ms_water_quality.application.services;
import pe.edu.vallegrande.ms_water_quality.domain.models.QualityParameter;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.QualityParameterCreateRequest;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.response.QualityParameterResponse;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.response.enriched.QualityParameterEnrichedResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface QualityParameterService {

    Flux<QualityParameterEnrichedResponse> getAll();
    Flux<QualityParameterEnrichedResponse> getAllActive();
    Flux<QualityParameterEnrichedResponse> getAllInactive();
    Mono<QualityParameterEnrichedResponse> getById(String id);
    
    Mono<QualityParameterResponse> save(QualityParameterCreateRequest request);
    Mono<QualityParameter> update(String id, QualityParameter updatedParameter);
    
    Mono<Void> delete(String id);
    Mono<QualityParameterEnrichedResponse> activate(String id);
    Mono<QualityParameterEnrichedResponse> deactivate(String id);
}
