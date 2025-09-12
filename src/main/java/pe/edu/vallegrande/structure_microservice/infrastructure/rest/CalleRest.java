package pe.edu.vallegrande.structure_microservice.infrastructure.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.structure_microservice.application.services.CalleService;
import pe.edu.vallegrande.structure_microservice.domain.models.Calle;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.ErrorMessage;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.ResponseDto;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.request.CalleCreateRequest;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.response.CalleResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/calles")
@CrossOrigin("*")
@AllArgsConstructor
public class CalleRest {

    private final CalleService calleService;

    @GetMapping
    public Mono<ResponseDto<List<Calle>>> getAll() {
        return calleService.getAll()
                .collectList()
                .map(calles -> new ResponseDto<>(true, calles));
    }

    @GetMapping("/active")
    public Mono<ResponseDto<List<Calle>>> getAllActive() {
        return calleService.getAllActive()
                .collectList()
                .map(calles -> new ResponseDto<>(true, calles));
    }

    @GetMapping("/{id}")
    public Mono<ResponseDto<Calle>> getById(@PathVariable String id) {
        return calleService.getById(id)
                .map(calle -> new ResponseDto<>(true, calle))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.NOT_FOUND.value(), "Calle not found", e.getMessage()))));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseDto<CalleResponse>> create(@RequestBody CalleCreateRequest request) {
        return calleService.save(request)
                .map(savedCalle -> new ResponseDto<>(true, savedCalle))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Validation error", e.getMessage()))));
    }

    @PutMapping("/{id}")
    public Mono<ResponseDto<Calle>> update(@PathVariable String id, @RequestBody Calle calle) {
        return calleService.update(id, calle)
                .map(updated -> new ResponseDto<>(true, updated))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Update failed", e.getMessage()))));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseDto<Object>> delete(@PathVariable String id) {
        return calleService.delete(id)
                .then(Mono.just(new ResponseDto<>(true, null)))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Delete failed", e.getMessage()))));
    }

    @PatchMapping("/{id}/activate")
    public Mono<ResponseDto<Calle>> activate(@PathVariable String id) {
        return calleService.activate(id)
                .map(calle -> new ResponseDto<>(true, calle))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Activation failed", e.getMessage()))));
    }

    @PatchMapping("/{id}/deactivate")
    public Mono<ResponseDto<Calle>> deactivate(@PathVariable String id) {
        return calleService.deactivate(id)
                .map(calle -> new ResponseDto<>(true, calle))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Deactivation failed", e.getMessage()))));
    }
}
