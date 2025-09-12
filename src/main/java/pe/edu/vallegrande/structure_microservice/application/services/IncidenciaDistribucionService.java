package pe.edu.vallegrande.structure_microservice.application.services;

import pe.edu.vallegrande.structure_microservice.domain.models.IncidenciaDistribucion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IncidenciaDistribucionService {
    Flux<IncidenciaDistribucion> listar();
    Mono<IncidenciaDistribucion> obtenerPorId(String id);
    Mono<IncidenciaDistribucion> registrar(IncidenciaDistribucion i);
    Mono<IncidenciaDistribucion> actualizar(String id, IncidenciaDistribucion i);
    Mono<Void> eliminar(String id);
}