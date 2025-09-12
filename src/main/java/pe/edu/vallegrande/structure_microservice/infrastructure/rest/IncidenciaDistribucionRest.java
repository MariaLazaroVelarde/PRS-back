package pe.edu.vallegrande.structure_microservice.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.structure_microservice.application.services.IncidenciaDistribucionService;
import pe.edu.vallegrande.structure_microservice.domain.models.IncidenciaDistribucion;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.ErrorMessage;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.ResponseDto;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/incidencias")
@CrossOrigin("*")
@RequiredArgsConstructor
public class IncidenciaDistribucionRest {

    private final IncidenciaDistribucionService service;

    @GetMapping
    public Mono<ResponseDto<List<IncidenciaDistribucion>>> getAll() {
        return service.listar()
                .collectList()
                .map(incidencias -> new ResponseDto<>(true, incidencias))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching incidencias", e.getMessage()))));
    }

    @GetMapping("/{id}")
    public Mono<ResponseDto<IncidenciaDistribucion>> getById(@PathVariable String id) {
        return service.obtenerPorId(id)
                .map(incidencia -> new ResponseDto<>(true, incidencia))
                .switchIfEmpty(Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.NOT_FOUND.value(), "Incidencia not found", "No incidencia found with ID: " + id))))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching incidencia", e.getMessage()))));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseDto<IncidenciaDistribucion>> create(@RequestBody IncidenciaDistribucion incidencia) {
        return service.registrar(incidencia)
                .map(saved -> new ResponseDto<>(true, saved))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Creation failed", e.getMessage()))));
    }

    @PutMapping("/{id}")
    public Mono<ResponseDto<IncidenciaDistribucion>> update(@PathVariable String id, @RequestBody IncidenciaDistribucion incidencia) {
        return service.actualizar(id, incidencia)
                .map(updated -> new ResponseDto<>(true, updated))
                .switchIfEmpty(Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.NOT_FOUND.value(), "Incidencia not found", "No incidencia found with ID: " + id))))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Update failed", e.getMessage()))));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseDto<Object>> delete(@PathVariable String id) {
        return service.eliminar(id)
                .then(Mono.just(new ResponseDto<>(true, null)))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Delete failed", e.getMessage()))));
    }
}
