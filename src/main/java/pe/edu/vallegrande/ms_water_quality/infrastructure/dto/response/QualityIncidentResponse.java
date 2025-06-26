package pe.edu.vallegrande.ms_water_quality.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualityIncidentResponse {

    private String id;
    private String organizationId;
    private String incidentCode;
    private String incidentType; // CLORO_BAJO, TURBIDEZ_ALTA, CONTAMINACION
    private String testingPointId;

    private LocalDateTime detectionDate;
    private String severity; // LOW, MEDIUM, HIGH, CRITICAL
    private String description;

    private List<String> affectedZones;

    private String immediateActions;
    private String correctiveActions;

    private boolean resolved;
    private LocalDateTime resolutionDate;

    private String reportedByUserId;
    private String resolvedByUserId;
}
