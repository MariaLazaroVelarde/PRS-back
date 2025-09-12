package pe.edu.vallegrande.ms_water_quality.infrastructure.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.ms_water_quality.application.services.QualityParameterService;
import pe.edu.vallegrande.ms_water_quality.domain.models.QualityParameter;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.ErrorMessage;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.ResponseDto;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.QualityParameterCreateRequest;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.response.QualityParameterResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/admin/quality/parameters")
@CrossOrigin("*")
public class QualityParameterRest {

    private final QualityParameterService service;

    public QualityParameterRest(QualityParameterService service) {
        this.service = service;
    }

    @GetMapping
    public Mono<ResponseDto<List<QualityParameter>>> getAll() {
        return service.getAll()
                .collectList()
                .map(list -> new ResponseDto<>(true, list));
    }

    @GetMapping("/active")
    public Mono<ResponseDto<List<QualityParameter>>> getAllActive() {
        return service.getAllActive()
                .collectList()
                .map(list -> new ResponseDto<>(true, list));
    }

    @GetMapping("/inactive")
    public Mono<ResponseDto<List<QualityParameter>>> getAllInactive() {
        return service.getAllInactive()
                .collectList()
                .map(list -> new ResponseDto<>(true, list));
    }

    @GetMapping("/{id}")
    public Mono<ResponseDto<QualityParameter>> getById(@PathVariable String id) {
        return service.getById(id)
                .map(param -> new ResponseDto<>(true, param))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false, new ErrorMessage(
                                HttpStatus.NOT_FOUND.value(),
                                "Quality parameter not found",
                                e.getMessage()))));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseDto<QualityParameterResponse>> create(@RequestBody QualityParameterCreateRequest request) {
        return service.save(request)
                .map(saved -> new ResponseDto<>(true, saved))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false, new ErrorMessage(
                                HttpStatus.BAD_REQUEST.value(),
                                "Validation error",
                                e.getMessage()))));
    }

    @PutMapping("/{id}")
    public Mono<ResponseDto<QualityParameter>> update(@PathVariable String id, @RequestBody QualityParameter request) {
        return service.update(id, request)
                .map(updated -> new ResponseDto<>(true, updated))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false, new ErrorMessage(
                                HttpStatus.BAD_REQUEST.value(),
                                "Update failed",
                                e.getMessage()))));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseDto<Object>> delete(@PathVariable String id) {
        return service.delete(id)
                .thenReturn(new ResponseDto<>(true, null))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false, new ErrorMessage(
                                HttpStatus.BAD_REQUEST.value(),
                                "Delete failed",
                                e.getMessage()))));
    }

    @PatchMapping("/{id}/activate")
    public Mono<ResponseDto<QualityParameter>> activate(@PathVariable String id) {
        return service.activate(id)
                .map(data -> new ResponseDto<>(true, data))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false, new ErrorMessage(
                                HttpStatus.BAD_REQUEST.value(),
                                "Activation failed",
                                e.getMessage()))));
    }

    @PatchMapping("/{id}/deactivate")
    public Mono<ResponseDto<QualityParameter>> deactivate(@PathVariable String id) {
        return service.deactivate(id)
                .map(data -> new ResponseDto<>(true, data))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false, new ErrorMessage(
                                HttpStatus.BAD_REQUEST.value(),
                                "Deactivation failed",
                                e.getMessage()))));
    }
}
