package pe.edu.vallegrande.ms_water_quality.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.ms_water_quality.application.services.QualityIncidentService;
import pe.edu.vallegrande.ms_water_quality.domain.models.QualityIncident;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.ErrorMessage;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.ResponseDto;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.QualityIncidentCreateRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/admin/quality/reports")
@CrossOrigin("*")
@RequiredArgsConstructor
public class QualityIncidentRest {

    private final QualityIncidentService service;

    @GetMapping
    public Mono<ResponseDto<List<QualityIncident>>> getAll() {
        return service.getAll().collectList()
                .map(list -> new ResponseDto<>(true, list));
    }

    @GetMapping("/resolved")
    public Mono<ResponseDto<List<QualityIncident>>> getResolved() {
        return service.getResolved().collectList()
                .map(list -> new ResponseDto<>(true, list));
    }

    @GetMapping("/unresolved")
    public Mono<ResponseDto<List<QualityIncident>>> getUnresolved() {
        return service.getUnresolved().collectList()
                .map(list -> new ResponseDto<>(true, list));
    }

    @GetMapping("/{id}")
    public Mono<ResponseDto<QualityIncident>> getById(@PathVariable String id) {
        return service.getById(id)
                .map(data -> new ResponseDto<>(true, data))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false, new ErrorMessage(
                                HttpStatus.NOT_FOUND.value(), "Get failed", e.getMessage()))));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseDto<QualityIncident>> create(@RequestBody QualityIncidentCreateRequest request) {
        return service.save(request)
                .map(data -> new ResponseDto<>(true, data))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false, new ErrorMessage(
                                HttpStatus.BAD_REQUEST.value(), "Create failed", e.getMessage()))));
    }

    @PutMapping("/{id}")
    public Mono<ResponseDto<QualityIncident>> update(@PathVariable String id, @RequestBody QualityIncident request) {
        return service.update(id, request)
                .map(data -> new ResponseDto<>(true, data))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false, new ErrorMessage(
                                HttpStatus.BAD_REQUEST.value(), "Update failed", e.getMessage()))));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseDto<Object>> delete(@PathVariable String id) {
        return service.delete(id)
                .thenReturn(new ResponseDto<>(true, null))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false, new ErrorMessage(
                                HttpStatus.BAD_REQUEST.value(), "Delete failed", e.getMessage()))));
    }
}
