package pe.edu.vallegrande.ms_water_quality.infrastructure.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.ms_water_quality.application.services.TestingPointService;
import pe.edu.vallegrande.ms_water_quality.domain.models.TestingPoint;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.ErrorMessage;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.ResponseDto;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.TestingPointCreateRequest;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.response.TestingPointResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/admin/quality/samplingpoints")
@CrossOrigin("*")
@AllArgsConstructor
public class TestingPointRest {

    private final TestingPointService service;

    @GetMapping
    public Mono<ResponseDto<List<TestingPoint>>> getAll() {
        return service.getAll()
                .collectList()
                .map(points -> new ResponseDto<>(true, points));
    }

    @GetMapping("/active")
    public Mono<ResponseDto<List<TestingPoint>>> getAllActive() {
        return service.getAllActive()
                .collectList()
                .map(points -> new ResponseDto<>(true, points));
    }

    @GetMapping("/inactive")
    public Mono<ResponseDto<List<TestingPoint>>> getAllInactive() {
        return service.getAllInactive()
                .collectList()
                .map(points -> new ResponseDto<>(true, points));
    }

    @GetMapping("/{id}")
    public Mono<ResponseDto<TestingPoint>> getById(@PathVariable String id) {
        return service.getById(id)
                .map(point -> new ResponseDto<>(true, point))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.NOT_FOUND.value(),
                                        "Testing Point not found", e.getMessage()))));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseDto<TestingPointResponse>> create(@RequestBody TestingPointCreateRequest request) {
        return service.save(request)
                .map(point -> new ResponseDto<>(true, point))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Validation error", e.getMessage()))));
    }

    @PutMapping("/{id}")
    public Mono<ResponseDto<TestingPoint>> update(@PathVariable String id, @RequestBody TestingPoint point) {
        return service.update(id, point)
                .map(updated -> new ResponseDto<>(true, updated))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Update failed", e.getMessage()))));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseDto<Object>> delete(@PathVariable String id) {
        return service.delete(id)
                .then(Mono.just(new ResponseDto<>(true, null)))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Delete failed", e.getMessage()))));
    }

    @PatchMapping("/{id}/activate")
    public Mono<ResponseDto<TestingPoint>> activate(@PathVariable String id) {
        return service.activate(id)
                .map(updated -> new ResponseDto<>(true, updated))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Activation failed", e.getMessage()))));
    }

    @PatchMapping("/{id}/deactivate")
    public Mono<ResponseDto<TestingPoint>> deactivate(@PathVariable String id) {
        return service.deactivate(id)
                .map(updated -> new ResponseDto<>(true, updated))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Deactivation failed", e.getMessage()))));
    }
}
