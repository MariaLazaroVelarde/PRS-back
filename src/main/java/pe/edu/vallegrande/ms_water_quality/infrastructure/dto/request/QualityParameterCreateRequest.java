package pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualityParameterCreateRequest {

    private String organizationId;

    private String parameterCode;
    private String parameterName;
    private String unitOfMeasure;

    private Double minAcceptable;
    private Double maxAcceptable;

    private OptimalRangeRequest optimalRange;

    private String testFrequency; // DAILY, WEEKLY, MONTHLY

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptimalRangeRequest {
        private Double min;
        private Double max;
    }
}
