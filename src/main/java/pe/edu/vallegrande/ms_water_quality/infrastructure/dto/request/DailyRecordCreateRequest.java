package pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyRecordCreateRequest {

    private String organizationId;
    private String recordCode; // Puede ser opcional si se genera automáticamente
    private List<String> testingPointIds;
    private LocalDateTime recordDate;
    private Double level;
    private boolean acceptable;
    private boolean actionRequired;
    private String recordedByUserId;
    private String observations;
    private Double amount;
    private String recordType; // "CLORO" o "SULFATO"
}

    // private LocalDateTime nextChlorinationDate;
