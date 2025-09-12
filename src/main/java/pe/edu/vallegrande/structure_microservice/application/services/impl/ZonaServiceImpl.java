package pe.edu.vallegrande.structure_microservice.application.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.structure_microservice.application.services.ZonaService;
import pe.edu.vallegrande.structure_microservice.domain.enums.Constants;
import pe.edu.vallegrande.structure_microservice.domain.models.Zona;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.request.ZonaCreateRequest;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.response.ZonaResponse;
import pe.edu.vallegrande.structure_microservice.infrastructure.exception.CustomException;
import pe.edu.vallegrande.structure_microservice.infrastructure.repository.ZonaRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ZonaServiceImpl implements ZonaService {

    @Autowired
    private ZonaRepository zonaRepository;

    @Override
    public Flux<Zona> getAll() {
        return zonaRepository.findAll()
                .doOnNext(zona -> log.info("Zona retrieved: {}", zona));
    }

    @Override
    public Flux<Zona> getAllActive() {
        return zonaRepository.findAllByStatus(Constants.ACTIVE.name());
    }

    @Override
    public Flux<Zona> getAllInactive() {
        return zonaRepository.findAllByStatus(Constants.INACTIVE.name());
    }

    @Override
    public Mono<Zona> getById(String id) {
        return zonaRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Zona not found",
                        "The requested zona with id " + id + " was not found")));
    }
@Override
public Mono<ZonaResponse> save(ZonaCreateRequest request) {
    Zona zona = new Zona();
    zona.setNombre(request.getNombre());
    zona.setDescripcion(request.getDescripcion());
    zona.setSedeId(request.getSedeId());
    zona.setStatus(Constants.ACTIVE.name());

    return zonaRepository.save(zona)
        .map(savedZona -> {
            ZonaResponse response = new ZonaResponse();
            response.setZonaId(savedZona.getZonaId());
            response.setNombre(savedZona.getNombre());
            response.setDescripcion(savedZona.getDescripcion()); // Complete this line
            return response; // Return the response object
        });
}

    @Override
    public Mono<Zona> update(String id, Zona zona) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Mono<Void> delete(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Mono<Zona> activate(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'activate'");
    }

    @Override
    public Mono<Zona> deactivate(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deactivate'");
    }
}