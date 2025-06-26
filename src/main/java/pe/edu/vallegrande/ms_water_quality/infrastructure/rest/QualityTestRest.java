package pe.edu.vallegrande.ms_water_quality.infrastructure.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.ms_water_quality.application.services.QualityTestService;
import pe.edu.vallegrande.ms_water_quality.domain.models.QualityTest;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.ErrorMessage;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.ResponseDto;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.QualityTestCreateRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v2/qualitytests")
@CrossOrigin("*")
@AllArgsConstructor
public class QualityTestRest {

    private final QualityTestService service;

    @GetMapping
    public Mono<ResponseDto<List<QualityTest>>> getAll() {
        return service.getAll()
                .collectList()
                .map(list -> new ResponseDto<>(true, list))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                        "Failed to retrieve quality tests", e.getMessage()))));
    }

    @GetMapping("/{id}")
    public Mono<ResponseDto<QualityTest>> getById(@PathVariable String id) {
        return service.getById(id)
                .map(result -> new ResponseDto<>(true, result))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.NOT_FOUND.value(),
                                        "Quality test not found", e.getMessage()))));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseDto<QualityTest>> create(@RequestBody QualityTestCreateRequest request) {
        return service.save(request)
                .map(saved -> new ResponseDto<>(true, saved))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Creation failed", e.getMessage()))));
    }

    @PutMapping("/{id}")
    public Mono<ResponseDto<QualityTest>> update(@PathVariable String id,
                                                 @RequestBody QualityTestCreateRequest request) {
        return service.update(id, request)
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
                .thenReturn(new ResponseDto<>(true, null))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Logical delete failed", e.getMessage()))));
    }

    @DeleteMapping("/{id}/physical")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseDto<Object>> deletePhysically(@PathVariable String id) {
        return service.deletePhysically(id)
                .thenReturn(new ResponseDto<>(true, null))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Physical delete failed", e.getMessage()))));
    }

    @PutMapping("/{id}/restore")
    public Mono<ResponseDto<QualityTest>> restore(@PathVariable String id) {
        return service.restore(id)
                .map(restored -> new ResponseDto<>(true, restored))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Restore failed", e.getMessage()))));
    }
}
