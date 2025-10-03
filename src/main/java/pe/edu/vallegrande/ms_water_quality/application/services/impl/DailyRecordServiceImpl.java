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
                return repository.findAll();
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
                if (request == null || request.getRecordType() == null || request.getOrganizationId() == null
                                || request.getRecordDate() == null) {
                        return Mono.error(new CustomException(
                                        HttpStatus.BAD_REQUEST.value(),
                                        "Invalid request",
                                        "Required fields: recordType, organizationId, and recordDate"));
                }

                return generateNextCode(request.getRecordType())
                                .flatMap(nextCode -> {
                                        DailyRecord record = new DailyRecord();
                                        record.setRecordType(request.getRecordType());
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

                                        return repository.save(record);
                                });
        }

        @Override
        public Mono<DailyRecord> update(String id, DailyRecordCreateRequest request) {
                if (request == null || request.getOrganizationId() == null || request.getRecordDate() == null) {
                        return Mono.error(
                                        new CustomException(HttpStatus.BAD_REQUEST.value(), "Invalid request",
                                                        "Required fields: organizationId and recordDate"));
                }

                return repository.findById(id)
                                .switchIfEmpty(Mono.error(new CustomException(
                                                HttpStatus.NOT_FOUND.value(),
                                                "Record not found",
                                                "The daily record with id " + id + " was not found")))
                                .flatMap(existing -> {
                                        existing.setOrganizationId(request.getOrganizationId());
                                        existing.setTestingPointIds(request.getTestingPointIds());
                                        existing.setRecordDate(request.getRecordDate());
                                        existing.setLevel(request.getLevel());
                                        existing.setAcceptable(request.isAcceptable());
                                        existing.setActionRequired(request.isActionRequired());
                                        existing.setRecordedByUserId(request.getRecordedByUserId());
                                        existing.setObservations(request.getObservations());
                                        existing.setAmount(request.getAmount());

                                        // Siempre generar un nuevo código
                                        return generateNextCode(request.getRecordType())
                                                        .flatMap(nextCode -> {
                                                                existing.setRecordCode(nextCode);
                                                                return repository.save(existing);
                                                        });
                                });
        }

        @Override
        public Mono<Void> delete(String id) {
                return repository.findById(id)
                                .switchIfEmpty(Mono.error(new CustomException(
                                                HttpStatus.NOT_FOUND.value(),
                                                "Record not found",
                                                "The daily record with id " + id + " was not found")))
                                .flatMap(existing -> {
                                        existing.setDeletedAt(LocalDateTime.now());
                                        return repository.save(existing)
                                                        .then();
                                });
        }

        @Override
        public Mono<Void> deletePhysically(String id) {
                return repository.findById(id)
                                .switchIfEmpty(Mono.error(new CustomException(
                                                HttpStatus.NOT_FOUND.value(),
                                                "Record not found",
                                                "The daily record with id " + id + " was not found")))
                                .flatMap(repository::delete);
        }

        @Override
        public Mono<DailyRecord> restore(String id) {
                return repository.findById(id)
                                .switchIfEmpty(Mono.error(new CustomException(
                                                HttpStatus.NOT_FOUND.value(),
                                                "Record not found",
                                                "The daily record with id " + id + " was not found")))
                                .flatMap(existing -> {
                                        if (existing.getDeletedAt() == null) {
                                                return Mono.error(new CustomException(
                                                                HttpStatus.CONFLICT.value(),
                                                                "Restore not needed",
                                                                "The record is already active"));
                                        }

                                        existing.setDeletedAt(null);
                                        return repository.save(existing);
                                });
        }

        // private Mono<String> generateNextCode() {
        // return repository.findAll()
        // .sort((s1, s2) -> s2.getRecordCode().compareTo(s1.getRecordCode()))
        // .next()
        // .map(last -> {
        // try {
        // String lastCode = last.getRecordCode(); // Ejemplo: "JASS007"
        // int number = Integer.parseInt(lastCode.replace("JASS", ""));
        // return String.format("JASS%03d", number + 1);
        // } catch (Exception e) {
        // return "JASS001";
        // }
        // })
        // .defaultIfEmpty("JASS001");
        // }

        private Mono<String> generateNextCode(String tipo) {
                String prefix = tipo.equalsIgnoreCase("CLORO") ? "CL" : "SA";

                return repository.findByRecordTypeOrderByRecordCodeDesc(tipo)
                                .next()
                                .map(last -> {
                                        try {
                                                String lastCode = last.getRecordCode(); // Ejemplo: "CL005"
                                                int number = Integer.parseInt(lastCode.replace(prefix, ""));
                                                return String.format("%s%03d", prefix, number + 1);
                                        } catch (Exception e) {
                                                return prefix + "001";
                                        }
                                })
                                .defaultIfEmpty(prefix + "001");
        }
}
