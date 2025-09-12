package pe.edu.vallegrande.structure_microservice.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.structure_microservice.application.services.IncidenciaDistribucionService;
import pe.edu.vallegrande.structure_microservice.domain.models.IncidenciaDistribucion;
import pe.edu.vallegrande.structure_microservice.infrastructure.repository.IncidenciaDistribucionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IncidenciaDistribucionServiceImpl implements IncidenciaDistribucionService {

    private final IncidenciaDistribucionRepository repository;

    @Override
    public Flux<IncidenciaDistribucion> listar() {
        return repository.findAll();
    }

    @Override
    public Mono<IncidenciaDistribucion> obtenerPorId(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<IncidenciaDistribucion> registrar(IncidenciaDistribucion i) {
        i.setFechaRegistro(LocalDateTime.now());
        return repository.save(i);
    }

    @Override
    public Mono<IncidenciaDistribucion> actualizar(String id, IncidenciaDistribucion i) {
        return repository.findById(id)
                .flatMap(existing -> {
                    i.setId(existing.getId());
                    return repository.save(i);
                });
    }

    @Override
    public Mono<Void> eliminar(String id) {
        return repository.deleteById(id);
    }
}