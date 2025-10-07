package pe.edu.vallegrande.ms_water_quality.infrastructure.dto.response.enriched;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.vallegrande.ms_water_quality.domain.models.QualityParameter;
import pe.edu.vallegrande.ms_water_quality.infrastructure.client.dto.ExternalOrganization;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QualityParameterEnrichedResponse {

    private String id;
    private String parameterCode;
    private String parameterName;
    private String unitOfMeasure;
    private Double minAcceptable;
    private Double maxAcceptable;
    private QualityParameter.OptimalRange optimalRange;
    private String testFrequency;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private ExternalOrganization organization;
}
