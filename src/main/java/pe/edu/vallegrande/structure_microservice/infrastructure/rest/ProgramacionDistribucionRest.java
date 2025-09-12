package pe.edu.vallegrande.structure_microservice.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.structure_microservice.application.services.ProgramacionDistribucionService;
import pe.edu.vallegrande.structure_microservice.domain.models.ProgramacionDistribucion;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.ErrorMessage;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.ResponseDto;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/programaciones")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ProgramacionDistribucionRest {

    private final ProgramacionDistribucionService service;

    @GetMapping
    public Mono<ResponseDto<List<ProgramacionDistribucion>>> getAll() {
        return service.listar()
                .collectList()
                .map(programaciones -> new ResponseDto<>(true, programaciones))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching programaciones", e.getMessage()))));
    }

    @GetMapping("/{id}")
    public Mono<ResponseDto<ProgramacionDistribucion>> getById(@PathVariable String id) {
        return service.obtenerPorId(id)
                .map(programacion -> new ResponseDto<>(true, programacion))
                .switchIfEmpty(Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.NOT_FOUND.value(), "Programación no encontrada", "No se encontró una programación con ID: " + id))))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error al buscar programación", e.getMessage()))));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseDto<ProgramacionDistribucion>> create(@RequestBody ProgramacionDistribucion p) {
        return service.registrar(p)
                .map(saved -> new ResponseDto<>(true, saved))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Error al crear programación", e.getMessage()))));
    }

    @PutMapping("/{id}")
    public Mono<ResponseDto<ProgramacionDistribucion>> update(@PathVariable String id, @RequestBody ProgramacionDistribucion p) {
        return service.actualizar(id, p)
                .map(updated -> new ResponseDto<>(true, updated))
                .switchIfEmpty(Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.NOT_FOUND.value(), "Programación no encontrada", "No se encontró una programación con ID: " + id))))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Error al actualizar programación", e.getMessage()))));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseDto<Object>> delete(@PathVariable String id) {
        return service.eliminar(id)
                .then(Mono.just(new ResponseDto<>(true, null)))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Error al eliminar programación", e.getMessage()))));
    }
}
