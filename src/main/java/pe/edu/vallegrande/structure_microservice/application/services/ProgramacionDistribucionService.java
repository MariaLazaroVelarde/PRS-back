package pe.edu.vallegrande.structure_microservice.application.services;

import pe.edu.vallegrande.structure_microservice.domain.models.ProgramacionDistribucion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProgramacionDistribucionService {
    Flux<ProgramacionDistribucion> listar();
    Mono<ProgramacionDistribucion> obtenerPorId(String id);
    Mono<ProgramacionDistribucion> registrar(ProgramacionDistribucion p);
    Mono<ProgramacionDistribucion> actualizar(String id, ProgramacionDistribucion p);
    Mono<Void> eliminar(String id);
}