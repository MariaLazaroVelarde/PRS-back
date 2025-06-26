package pe.edu.vallegrande.ms_water_quality.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.QualityTestCreateRequest.ResultItem;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "quality_tests")
public class QualityTest {

    @Id
    private String id;

    private String organizationId;
    private String testCode;
    private String testingPointId;
    private LocalDateTime testDate;
    private String testType; // RUTINARIO, ESPECIAL, INCIDENCIA
    private String testedByUserId;
    private String weatherConditions;
    private Double waterTemperature;
    private String generalObservations;
    private String status; // COMPLETED
    private List<TestResult> results;

    private LocalDateTime createdAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestResult {
        private String parameterId;
        private String parameterCode;
        private Double measuredValue;
        private String unit;
        private String status; // ACCEPTABLE, WARNING, CRITICAL
        private String observations;
    }
}
