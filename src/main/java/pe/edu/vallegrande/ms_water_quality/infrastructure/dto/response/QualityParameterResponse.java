package pe.edu.vallegrande.ms_water_quality.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualityParameterResponse {
    private String id;
    private String parameterCode;
    private String parameterName;
    private String testFrequency;
    private String status;
    private LocalDateTime createdAt;
}
