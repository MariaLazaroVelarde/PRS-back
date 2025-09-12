package pe.edu.vallegrande.ms_water_quality.application.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.ms_water_quality.application.services.QualityTestService;
import pe.edu.vallegrande.ms_water_quality.domain.models.QualityTest;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.QualityTestCreateRequest;
import pe.edu.vallegrande.ms_water_quality.infrastructure.exception.CustomException;
import pe.edu.vallegrande.ms_water_quality.infrastructure.repository.QualityTestRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class QualityTestServiceImpl implements QualityTestService {

    private final QualityTestRepository qualityTestRepository;

    /** GET /api/admin/quality/tests */
    @Override
    public Flux<QualityTest> getAll() {
        return qualityTestRepository.findAll()
                .doOnNext(test -> log.info("Quality test found: {}", test.getId()));
    }

    /** GET /api/admin/quality/tests/{id} */
    @Override
    public Mono<QualityTest> getById(String id) {
        return qualityTestRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Quality test not found",
                        "No quality test found with id " + id)));
    }

    /** POST /api/admin/quality/tests */
    @Override
    public Mono<QualityTest> save(QualityTestCreateRequest request) {
        return generateNextCode().flatMap(generatedCode -> {
            QualityTest qualityTest = new QualityTest();

            qualityTest.setOrganizationId(request.getOrganizationId());
            qualityTest.setTestCode(generatedCode);
            qualityTest.setTestingPointId(request.getTestingPointId());
            qualityTest.setTestDate(request.getTestDate());
            qualityTest.setTestType(request.getTestType());
            qualityTest.setTestedByUserId(request.getTestedByUserId());
            qualityTest.setWeatherConditions(request.getWeatherConditions());
            qualityTest.setWaterTemperature(request.getWaterTemperature());
            qualityTest.setGeneralObservations(request.getGeneralObservations());
            qualityTest.setStatus("COMPLETED");
            qualityTest.setCreatedAt(LocalDateTime.now());
            qualityTest.setDeletedAt(null);

            List<QualityTest.TestResult> results = request.getResults().stream()
                    .map(item -> {
                        QualityTest.TestResult result = new QualityTest.TestResult();
                        result.setParameterId(item.getParameterId());
                        result.setParameterCode(item.getParameterCode());
                        result.setMeasuredValue(item.getMeasuredValue());
                        result.setUnit(item.getUnit());
                        result.setStatus(item.getStatus());
                        result.setObservations(item.getObservations());
                        return result;
                    })
                    .collect(Collectors.toList());

            qualityTest.setResults(results);

            return qualityTestRepository.save(qualityTest);
        });
    }

    /** PUT /api/admin/quality/tests/{id} */
    @Override
    public Mono<QualityTest> update(String id, QualityTestCreateRequest request) {
        return qualityTestRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Quality test not found",
                        "No quality test found with id " + id)))
                .flatMap(existing -> generateNextCode().flatMap(generatedCode -> {
                    existing.setOrganizationId(request.getOrganizationId());
                    existing.setTestingPointId(request.getTestingPointId());
                    existing.setTestDate(request.getTestDate());
                    existing.setTestType(request.getTestType());
                    existing.setTestedByUserId(request.getTestedByUserId());
                    existing.setWeatherConditions(request.getWeatherConditions());
                    existing.setWaterTemperature(request.getWaterTemperature());
                    existing.setGeneralObservations(request.getGeneralObservations());
                    existing.setStatus(request.getStatus());
                    existing.setTestCode(generatedCode);

                    List<QualityTest.TestResult> results = request.getResults().stream()
                            .map(item -> new QualityTest.TestResult(
                                    item.getParameterId(),
                                    item.getParameterCode(),
                                    item.getMeasuredValue(),
                                    item.getUnit(),
                                    item.getStatus(),
                                    item.getObservations()))
                            .collect(Collectors.toList());

                    existing.setResults(results);

                    return qualityTestRepository.save(existing);
                }));
    }

    /** DELETE /api/admin/quality/tests/{id} → Lógico */
    @Override
    public Mono<Void> delete(String id) {
        return qualityTestRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Quality test not found",
                        "No quality test found with id " + id)))
                .flatMap(test -> {
                    test.setDeletedAt(LocalDateTime.now());
                    return qualityTestRepository.save(test);
                })
                .then();
    }

    /** DELETE /api/admin/quality/tests/{id}/hard → Físico */
    @Override
    public Mono<Void> deletePhysically(String id) {
        return qualityTestRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Quality test not found",
                        "No quality test found with id " + id)))
                .flatMap(qualityTestRepository::delete);
    }

    /** PATCH /api/admin/quality/tests/{id}/restore */
    @Override
    public Mono<QualityTest> restore(String id) {
        return qualityTestRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Quality test not found",
                        "No quality test found with id " + id)))
                .flatMap(test -> {
                    test.setDeletedAt(null);
                    return qualityTestRepository.save(test);
                });
    }

    /** Código incremental tipo ANL### */
    private Mono<String> generateNextCode() {
        return qualityTestRepository.findAll()
                .filter(t -> t.getTestCode() != null && t.getTestCode().startsWith("ANL"))
                .sort((t1, t2) -> t2.getTestCode().compareTo(t1.getTestCode()))
                .next()
                .map(last -> {
                    try {
                        int number = Integer.parseInt(last.getTestCode().replace("ANL", ""));
                        return String.format("ANL%03d", number + 1);
                    } catch (Exception e) {
                        return "ANL001";
                    }
                })
                .defaultIfEmpty("ANL001");
    }
}
