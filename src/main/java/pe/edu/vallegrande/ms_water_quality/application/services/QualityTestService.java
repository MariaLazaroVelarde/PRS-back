package pe.edu.vallegrande.ms_water_quality.application.services;

import pe.edu.vallegrande.ms_water_quality.domain.models.QualityTest;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.QualityTestCreateRequest;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.response.enriched.QualityTestEnrichedResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface QualityTestService {
    Flux<QualityTest> getAll();

    Mono<QualityTestEnrichedResponse> getById(String id);

    Mono<QualityTest> save(QualityTestCreateRequest request);

    Mono<QualityTest> update(String id, QualityTestCreateRequest request);

    Mono<Void> delete(String id);

    Mono<Void> deletePhysically(String id);

    Mono<QualityTest> restore(String id); // <- Este podría estar faltando
}
