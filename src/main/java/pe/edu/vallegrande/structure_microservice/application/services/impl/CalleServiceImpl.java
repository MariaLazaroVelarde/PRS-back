package pe.edu.vallegrande.structure_microservice.application.services.impl;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.structure_microservice.application.services.CalleService;
import pe.edu.vallegrande.structure_microservice.domain.enums.Constants;
import pe.edu.vallegrande.structure_microservice.domain.models.Calle;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.request.CalleCreateRequest;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.response.CalleResponse;
import pe.edu.vallegrande.structure_microservice.infrastructure.exception.CustomException;
import pe.edu.vallegrande.structure_microservice.infrastructure.repository.CalleRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CalleServiceImpl implements CalleService {

    @Autowired
    private CalleRepository calleRepository;

    @Override
    public Flux<Calle> getAll() {
        return calleRepository.findAll()
                .doOnNext(calle -> log.info("Calle retrieved: {}", calle));
    }
@Override
public Flux<Calle> getAllActive() {
    return calleRepository.findAllByEstado(true);  // Usamos true para las activas
}

@Override
public Flux<Calle> getAllInactive() {
    return calleRepository.findAllByEstado(false);  // Usamos false para las inactivas
}


    @Override
    public Mono<Calle> getById(String id) {
        return calleRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Calle not found",
                        "The requested calle with id " + id + " was not found")));
    }

 @Override
public Mono<CalleResponse> save(CalleCreateRequest request) {
    Calle calle = new Calle();
    calle.setNombre(request.getNombre());
    calle.setZonaId(request.getZonaId());
    calle.setZonaNombre(request.getZonaNombre());
    calle.setEstado(true); // Estado activo por defecto
    calle.setFechaRegistro(LocalDateTime.now()); // Establecer la fecha actual

    return calleRepository.save(calle)
        .map(savedCalle -> {
            CalleResponse response = new CalleResponse();
            response.setCalleId(savedCalle.getCalleId());
            response.setNombre(savedCalle.getNombre());
            response.setZonaId(savedCalle.getZonaId());
            response.setZonaNombre(savedCalle.getZonaNombre());
            response.setEstado(savedCalle.isEstado());
            response.setFechaRegistro(savedCalle.getFechaRegistro());
            return response;
        });
}


    @Override
    public Mono<Calle> update(String id, Calle calle) {
        return calleRepository.findById(id)
                .flatMap(existingCalle -> {
                    existingCalle.setNombre(calle.getNombre());
                    existingCalle.setZonaId(calle.getZonaId());
                    existingCalle.setZonaNombre(calle.getZonaNombre());
                    return calleRepository.save(existingCalle);
                })
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Calle not found",
                        "Cannot update calle with id " + id)));
    }

    @Override
    public Mono<Void> delete(String id) {
        return calleRepository.deleteById(id);
    }

    @Override
    public Mono<Calle> activate(String id) {
        return calleRepository.findById(id)
                .flatMap(calle -> {
                    calle.setEstado(true);
                    return calleRepository.save(calle);
                })
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Calle not found",
                        "Cannot activate calle with id " + id)));
    }

    @Override
    public Mono<Calle> deactivate(String id) {
        return calleRepository.findById(id)
                .flatMap(calle -> {
                    calle.setEstado(false);
                    return calleRepository.save(calle);
                })
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Calle not found",
                        "Cannot deactivate calle with id " + id)));
    }
}
