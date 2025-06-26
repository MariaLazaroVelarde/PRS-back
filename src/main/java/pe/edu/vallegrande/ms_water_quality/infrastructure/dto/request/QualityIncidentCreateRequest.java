package pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QualityIncidentCreateRequest {
    private String organizationId;
    private String incidentCode;
    private String incidentType;
    private String testingPointId;
    private LocalDateTime detectionDate;
    private String severity;
    private String description;
    private List<String> affectedZones;
    private String immediateActions;
    private String correctiveActions;
    private Boolean resolved;
    private LocalDateTime resolutionDate;
    private String reportedByUserId;
    private String resolvedByUserId;
}
