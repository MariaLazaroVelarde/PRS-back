package pe.edu.vallegrande.structure_microservice.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.structure_microservice.application.services.ProgramacionDistribucionService;
import pe.edu.vallegrande.structure_microservice.domain.models.ProgramacionDistribucion;
import pe.edu.vallegrande.structure_microservice.infrastructure.repository.ProgramacionDistribucionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ProgramacionDistribucionServiceImpl implements ProgramacionDistribucionService {

    private final ProgramacionDistribucionRepository repository;

    @Override
    public Flux<ProgramacionDistribucion> listar() {
        return repository.findAll();
    }

    @Override
    public Mono<ProgramacionDistribucion> obtenerPorId(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<ProgramacionDistribucion> registrar(ProgramacionDistribucion p) {
        p.setFechaRegistro(LocalDate.now());
        return repository.save(p);
    }

    @Override
    public Mono<ProgramacionDistribucion> actualizar(String id, ProgramacionDistribucion p) {
        return repository.findById(id)
                .flatMap(existing -> {
                    p.setId(existing.getId());
                    return repository.save(p);
                });
    }

    @Override
    public Mono<Void> eliminar(String id) {
        return repository.deleteById(id);
    }
}