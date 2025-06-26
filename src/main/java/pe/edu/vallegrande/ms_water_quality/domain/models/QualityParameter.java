package pe.edu.vallegrande.ms_water_quality.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.QualityParameterCreateRequest.OptimalRangeRequest;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "quality_parameters")
public class QualityParameter {

    @Id
    private String id;

    private String organizationId;

    private String parameterCode;
    private String parameterName;
    private String unitOfMeasure;

    private Double minAcceptable;
    private Double maxAcceptable;

    private OptimalRange optimalRange;

    private String testFrequency; // DAILY, WEEKLY, MONTHLY
    private String status = "ACTIVE";

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    // Clase interna para el rango óptimo
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptimalRange {
        private Double min;
        private Double max;
    }
}
