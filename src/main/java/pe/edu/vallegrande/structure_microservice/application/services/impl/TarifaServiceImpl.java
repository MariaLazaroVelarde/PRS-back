package pe.edu.vallegrande.structure_microservice.application.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import pe.edu.vallegrande.structure_microservice.application.services.TarifaService;
import pe.edu.vallegrande.structure_microservice.domain.enums.Constants;
import pe.edu.vallegrande.structure_microservice.domain.models.Tarifa;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.request.TarifaCreateRequest;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.response.TarifaResponse;
import pe.edu.vallegrande.structure_microservice.infrastructure.exception.CustomException;
import pe.edu.vallegrande.structure_microservice.infrastructure.repository.TarifaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class TarifaServiceImpl implements TarifaService {

    @Autowired
    private TarifaRepository tarifaRepository;


    @Override
    public Flux<Tarifa> getAll() {
        return tarifaRepository.findAll()
                .doOnNext(tarifa -> log.info("Tarifa retrieved: {}", tarifa));
    }

    @Override
    public Flux<Tarifa> getAllActive() {
        return tarifaRepository.findAllByStatus(Constants.ACTIVE.name());
    }

    @Override
    public Flux<Tarifa> getAllInactive() {
        return tarifaRepository.findAllByStatus(Constants.INACTIVE.name());
    }

    @Override
    public Mono<Tarifa> getById(String id) {
        return tarifaRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Tarifa not found",
                        "The requested tarifa with id " + id + " was not found")));
    }

    @Override
    public Mono<TarifaResponse> save(TarifaCreateRequest request) {
        return tarifaRepository.existsByZonaId(request.getZonaId())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new CustomException(
                                HttpStatus.BAD_REQUEST.value(),
                                "Zona already exists",
                                "The zona " + request.getZonaId() + " is already registered"));
                    }

                    Tarifa tarifa = new Tarifa();
                    tarifa.setZonaId(request.getZonaId());
                    tarifa.setMonto(request.getMonto());
                    tarifa.setDescripcion(request.getDescripcion());
                    tarifa.setTipoTarifa(request.getTipoTarifa());
                    tarifa.setFechaInicioVigencia(request.getFechaInicioVigencia());
                    tarifa.setFechaFinVigencia(request.getFechaFinVigencia());
                    tarifa.setStatus(Constants.ACTIVE.name());
                    tarifa.setZonaNombre(request.getZonaNombre());

                    return tarifaRepository.save(tarifa)
                            .map(savedTarifa -> {
                                TarifaResponse response = new TarifaResponse();
                                response.setZonaId(savedTarifa.getTarifaId());
                                response.setZonaId(savedTarifa.getZonaId());
                                response.setMonto(savedTarifa.getMonto());
                                response.setDescripcion(savedTarifa.getDescripcion());
                                response.setFechaInicioVigencia(savedTarifa.getFechaInicioVigencia());
                                response.setFechaFinVigencia(savedTarifa.getFechaFinVigencia());
                                response.setStatus(savedTarifa.getStatus());
                                response.setFechaRegistro(savedTarifa.getFechaRegistro());
                                response.setZonaNombre(savedTarifa.getZonaNombre());
                                return response;
                            });
                });
    }

    @Override
    public Mono<Tarifa> update(String id, Tarifa tarifa) {
        return tarifaRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Tarifa not found",
                        "Cannot update non-existent tarifa with id " + id)))
                .flatMap(existingTarifa -> {
                    existingTarifa.setZonaId(tarifa.getZonaId());
                    existingTarifa.setMonto(tarifa.getMonto());
                    existingTarifa.setDescripcion(tarifa.getDescripcion());
                    existingTarifa.setTipoTarifa(tarifa.getTipoTarifa());
                    existingTarifa.setFechaInicioVigencia(tarifa.getFechaInicioVigencia());
                    existingTarifa.setFechaFinVigencia(tarifa.getFechaFinVigencia());
                    existingTarifa.setZonaNombre(tarifa.getZonaNombre());
                    return tarifaRepository.save(existingTarifa);
                });
    }

    @Override
    public Mono<Void> delete(String id) {
        return tarifaRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Tarifa not found",
                        "Cannot delete non-existent tarifa with id " + id)))
                .flatMap(tarifaRepository::delete);
    }

    @Override
    public Mono<Tarifa> activate(String id) {
        return changeStatus(id, Constants.ACTIVE.name());
    }

    @Override
    public Mono<Tarifa> deactivate(String id) {
        return changeStatus(id, Constants.INACTIVE.name());
    }

    private Mono<Tarifa> changeStatus(String id, String status) {
        return tarifaRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Tarifa not found",
                        "Cannot change status of non-existent tarifa with id " + id)))
                .flatMap(tarifa -> {
                    tarifa.setStatus(status);
                    //user.setUpdatedAt(LocalDateTime.now());
                    return tarifaRepository.save(tarifa);
                });
    }
}