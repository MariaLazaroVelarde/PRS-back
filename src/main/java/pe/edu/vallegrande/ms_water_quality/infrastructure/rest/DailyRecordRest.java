package pe.edu.vallegrande.ms_water_quality.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.ms_water_quality.application.services.DailyRecordService;
import pe.edu.vallegrande.ms_water_quality.domain.models.DailyRecord;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.ErrorMessage;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.ResponseDto;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.DailyRecordCreateRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v2/dailyrecords")
@CrossOrigin("*")
@RequiredArgsConstructor
public class DailyRecordRest {

    private final DailyRecordService dailyRecordService;

    @GetMapping
    public Mono<ResponseDto<List<DailyRecord>>> getAll() {
        return dailyRecordService.getAll()
                .collectList()
                .map(records -> new ResponseDto<>(true, records));
    }

    @GetMapping("/{id}")
    public Mono<ResponseDto<DailyRecord>> getById(@PathVariable String id) {
        return dailyRecordService.getById(id)
                .map(record -> new ResponseDto<>(true, record))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.NOT_FOUND.value(),
                                        "Record not found",
                                        e.getMessage()))));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseDto<DailyRecord>> create(@RequestBody DailyRecordCreateRequest request) {
        return dailyRecordService.save(request)
                .map(record -> new ResponseDto<>(true, record))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Creation failed",
                                        e.getMessage()))));
    }

    @PutMapping("/{id}")
    public Mono<ResponseDto<DailyRecord>> update(
            @PathVariable String id,
            @RequestBody DailyRecordCreateRequest request) {

        return dailyRecordService.update(id, request)
                .map(updated -> new ResponseDto<>(true, updated))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Update failed", e.getMessage()))));
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseDto<Object>> delete(@PathVariable String id) {
        return dailyRecordService.delete(id)
                .then(Mono.just(new ResponseDto<>(true, null)))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Delete failed",
                                        e.getMessage()))));
    }
}
