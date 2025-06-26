package pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List; // Importar las clases necesarias

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyRecordCreateRequest {
    private String organizationId;
    private String recordCode;
    private List<String> testingPointIds; // Usar List en lugar de Array
    private LocalDateTime recordDate;
    private Double level;
    private boolean acceptable;
    private boolean actionRequired;
    private String recordedByUserId;
    private String observations;
    private Double amount;
    // private LocalDateTime nextChlorinationDate; // Puedes descomentar si es necesario
}
