package pe.edu.vallegrande.ms_water_quality.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChlorineRecordResponse {

    private String id;
    private String organizationId;
    private String recordCode;
    private String testingPointId;

    private LocalDateTime recordDate;
    private Double chlorineLevel;
    private boolean acceptable;
    private boolean actionRequired;

    private String recordedByUserId;
    private String observations;
    private LocalDateTime nextChlorinationDate;

    private LocalDateTime createdAt;
}
