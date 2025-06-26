package pe.edu.vallegrande.ms_water_quality.application.services;
import pe.edu.vallegrande.ms_water_quality.domain.models.QualityParameter;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.QualityParameterCreateRequest;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.response.QualityParameterResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface QualityParameterService {

    Flux<QualityParameter> getAll();
    Flux<QualityParameter> getAllActive();
    Flux<QualityParameter> getAllInactive();
    Mono<QualityParameter> getById(String id);
    
    Mono<QualityParameterResponse> save(QualityParameterCreateRequest request);
    Mono<QualityParameter> update(String id, QualityParameter updatedParameter);
    
    Mono<Void> delete(String id);
    Mono<QualityParameter> activate(String id);
    Mono<QualityParameter> deactivate(String id);
}
