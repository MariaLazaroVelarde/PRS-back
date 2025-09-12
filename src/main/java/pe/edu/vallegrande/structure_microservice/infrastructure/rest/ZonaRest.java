package pe.edu.vallegrande.structure_microservice.infrastructure.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.structure_microservice.application.services.ZonaService;
import pe.edu.vallegrande.structure_microservice.domain.models.Zona;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.ErrorMessage;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.ResponseDto;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.request.ZonaCreateRequest;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.response.ZonaResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/zonas")
@CrossOrigin("*")
@AllArgsConstructor
public class ZonaRest {

    private final ZonaService zonaService;

    @GetMapping
    public Mono<ResponseDto<List<Zona>>> getAll() {
        return zonaService.getAll()
                .collectList()
                .map(zonas -> new ResponseDto<>(true, zonas));
    }

    @GetMapping("/active")
    public Mono<ResponseDto<List<Zona>>> getAllActive() {
        return zonaService.getAllActive()
                .collectList()
                .map(zonas -> new ResponseDto<>(true, zonas));
    }

    @GetMapping("/{id}")
    public Mono<ResponseDto<Zona>> getById(@PathVariable String id) {
        return zonaService.getById(id)
                .map(zona -> new ResponseDto<>(true, zona))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.NOT_FOUND.value(), "Zona not found", e.getMessage()))));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseDto<ZonaResponse>> create(@RequestBody ZonaCreateRequest request) {
        return zonaService.save(request)
                .map(savedZona -> new ResponseDto<>(true, savedZona))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Validation error", e.getMessage()))));
    }

    @PutMapping("/{id}")
    public Mono<ResponseDto<Zona>> update(@PathVariable String id, @RequestBody Zona zona) {
        return zonaService.update(id, zona)
                .map(updated -> new ResponseDto<>(true, updated))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Update failed", e.getMessage()))));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseDto<Object>> delete(@PathVariable String id) {
        return zonaService.delete(id)
                .then(Mono.just(new ResponseDto<>(true, null)))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Delete failed", e.getMessage()))));
    }

    @PatchMapping("/{id}/activate")
    public Mono<ResponseDto<Zona>> activate(@PathVariable String id) {
        return zonaService.activate(id)
                .map(zona -> new ResponseDto<>(true, zona))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Activation failed", e.getMessage()))));
    }

    @PatchMapping("/{id}/deactivate")
    public Mono<ResponseDto<Zona>> deactivate(@PathVariable String id) {
        return zonaService.deactivate(id)
                .map(zona -> new ResponseDto<>(true, zona))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Deactivation failed", e.getMessage()))));
    }
}
