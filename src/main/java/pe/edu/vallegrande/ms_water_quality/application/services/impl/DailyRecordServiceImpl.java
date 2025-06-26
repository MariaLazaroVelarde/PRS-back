package pe.edu.vallegrande.ms_water_quality.application.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.ms_water_quality.application.services.DailyRecordService;
import pe.edu.vallegrande.ms_water_quality.domain.models.DailyRecord;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.DailyRecordCreateRequest;
import pe.edu.vallegrande.ms_water_quality.infrastructure.exception.CustomException;
import pe.edu.vallegrande.ms_water_quality.infrastructure.repository.DailyRecordRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class DailyRecordServiceImpl implements DailyRecordService {

    private final DailyRecordRepository repository;

    @Override
    public Flux<DailyRecord> getAll() {
        return repository.findAll()
                .doOnNext(record -> log.info("Record found: {}", record));
    }

    @Override
    public Mono<DailyRecord> getById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Record not found",
                        "The daily record with id " + id + " was not found")));
    }

    @Override
    public Mono<DailyRecord> save(DailyRecordCreateRequest request) {
        if (request == null) {
            return Mono.error(new CustomException(HttpStatus.BAD_REQUEST.value(), "Invalid request", "Request cannot be null"));
        }

        return generateNextCode()
                .flatMap(nextCode -> {
                    DailyRecord record = new DailyRecord();
                    record.setOrganizationId(request.getOrganizationId());
                    record.setRecordCode(nextCode);
                    record.setTestingPointIds(request.getTestingPointIds());
                    record.setRecordDate(request.getRecordDate());
                    record.setLevel(request.getLevel());
                    record.setAcceptable(request.isAcceptable());
                    record.setActionRequired(request.isActionRequired());
                    record.setRecordedByUserId(request.getRecordedByUserId());
                    record.setObservations(request.getObservations());
                    record.setAmount(request.getAmount());
                    record.setCreatedAt(LocalDateTime.now());

                    return repository.save(record)
                            .doOnSuccess(savedRecord -> log.info("Record saved: {}", savedRecord));
                });
    }

    @Override
    public Mono<DailyRecord> update(String id, DailyRecordCreateRequest request) {
        if (request == null) {
            return Mono.error(new CustomException(HttpStatus.BAD_REQUEST.value(), "Invalid request", "Request cannot be null"));
        }

        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Record not found",
                        "The daily record with id " + id + " was not found")))
                .flatMap(existing -> {
                    existing.setOrganizationId(request.getOrganizationId());
                    existing.setRecordCode(request.getRecordCode());
                    existing.setTestingPointIds(request.getTestingPointIds());
                    existing.setRecordDate(request.getRecordDate());
                    existing.setLevel(request.getLevel());
                    existing.setAcceptable(request.isAcceptable());
                    existing.setActionRequired(request.isActionRequired());
                    existing.setRecordedByUserId(request.getRecordedByUserId());
                    existing.setObservations(request.getObservations());
                    existing.setAmount(request.getAmount());

                    return repository.save(existing)
                            .doOnSuccess(updatedRecord -> log.info("Record updated: {}", updatedRecord));
                });
    }
@Override
public Mono<Void> deletePhysically(String id) {
    return repository.findById(id)
            .switchIfEmpty(Mono.error(new CustomException(
                    HttpStatus.NOT_FOUND.value(),
                    "Record not found",
                    "The daily record with id " + id + " was not found")))
            .flatMap(repository::delete)
            .doOnSuccess(aVoid -> log.info("Record physically deleted: {}", id));
}

@Override
public Mono<DailyRecord> restore(String id) {
    return repository.findById(id)
            .switchIfEmpty(Mono.error(new CustomException(
                    HttpStatus.NOT_FOUND.value(),
                    "Record not found",
                    "The daily record with id " + id + " was not found")))
            .flatMap(existing -> {
                // Aquí debes implementar la lógica de restauración del registro
                // Por ejemplo, puedes eliminar la marca de eliminación lógica
                existing.setDeletedAt(null);
                return repository.save(existing)
                        .doOnSuccess(restoredRecord -> log.info("Record restored: {}", restoredRecord));
            });
}

    @Override
    public Mono<Void> delete(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Record not found",
                        "The daily record with id " + id + " was not found")))
                .flatMap(existing -> repository.delete(existing)
                        .doOnSuccess(aVoid -> log.info("Record deleted: {}", id)));
    }

    private Mono<String> generateNextCode() {
        return repository.findAll()
                .sort((s1, s2) -> s2.getRecordCode().compareTo(s1.getRecordCode())) // ordenar descendente por código de registro
                .next()
                .map(last -> {
                    String lastCode = last.getRecordCode(); // Ejemplo: "JASS007"
                    int number = Integer.parseInt(lastCode.replace("JASS", ""));
                    return String.format("JASS%03d", number + 1);
                })
                .defaultIfEmpty("JASS001"); // Si es el primero
    }
}
