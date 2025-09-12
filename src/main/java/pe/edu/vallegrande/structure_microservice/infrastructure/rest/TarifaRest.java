package pe.edu.vallegrande.structure_microservice.infrastructure.rest;

import lombok.AllArgsConstructor;
import pe.edu.vallegrande.structure_microservice.application.services.TarifaService;
import pe.edu.vallegrande.structure_microservice.domain.models.Tarifa;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.ErrorMessage;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.ResponseDto;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.request.TarifaCreateRequest;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.response.TarifaResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tarifas")
@CrossOrigin("*")
@AllArgsConstructor
public class TarifaRest {

    private final TarifaService tarifaService;

    @GetMapping
    public Mono<ResponseDto<List<Tarifa>>> getAll() {
        return tarifaService.getAll()
                .collectList()
                .map(tarifas -> new ResponseDto<>(true, tarifas));
    }

    @GetMapping("/active")
    public Mono<ResponseDto<List<Tarifa>>> getAllActive() {
        return tarifaService.getAllActive()
                .collectList()
                .map(tarifas -> new ResponseDto<>(true, tarifas));
    }

    @GetMapping("/inactive")
    public Mono<ResponseDto<List<Tarifa>>> getAllInactive() {
        return tarifaService.getAllInactive()
                .collectList()
                .map(tarifas -> new ResponseDto<>(true, tarifas));
    }

    @GetMapping("/{id}")
    public Mono<ResponseDto<Tarifa>> getById(@PathVariable String id) {
        return tarifaService.getById(id)
                .map(tarifa -> new ResponseDto<>(true, tarifa))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.NOT_FOUND.value(),
                                        "User not found",
                                        e.getMessage()))));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseDto<TarifaResponse>> create(@RequestBody TarifaCreateRequest request) {
        return tarifaService.save(request)
                .map(savedTarifa -> new ResponseDto<>(true, savedTarifa))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Validation error",
                                        e.getMessage()))));
    }
    @PutMapping("/{id}")
    public Mono<ResponseDto<Tarifa>> update(@PathVariable String id, @RequestBody Tarifa tarifa) {
        return tarifaService.update(id, tarifa)
                .map(updatedTarifa -> new ResponseDto<>(true, updatedTarifa))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Update failed",
                                        e.getMessage()))));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseDto<Object>> delete(@PathVariable String id) {
        return tarifaService.delete(id)
                .then(Mono.just(new ResponseDto<>(true, null)))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Delete failed",
                                        e.getMessage()))));
    }

    @PatchMapping("/{id}/activate")
    public Mono<ResponseDto<Tarifa>> activate(@PathVariable String id) {
        return tarifaService.activate(id)
                .map(tarifa -> new ResponseDto<>(true, tarifa))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Activation failed",
                                        e.getMessage()))));
    }

    @PatchMapping("/{id}/deactivate")
    public Mono<ResponseDto<Tarifa>> deactivate(@PathVariable String id) {
        return tarifaService.deactivate(id)
                .map(tarifa -> new ResponseDto<>(true, tarifa))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Deactivation failed",
                                        e.getMessage()))));
    }


}