package pe.edu.vallegrande.ms_water_quality.application.services;

import pe.edu.vallegrande.ms_water_quality.domain.models.DailyRecord;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.DailyRecordCreateRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DailyRecordService {
    Flux<DailyRecord> getAll();
    Mono<DailyRecord> getById(String id);
    Mono<DailyRecord> save(DailyRecordCreateRequest request);
    Mono<DailyRecord> update(String id, DailyRecordCreateRequest request);
    Mono<Void> delete(String id);
}
