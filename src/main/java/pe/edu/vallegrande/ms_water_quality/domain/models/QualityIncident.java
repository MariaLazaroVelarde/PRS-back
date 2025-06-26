package pe.edu.vallegrande.ms_water_quality.domain.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "quality_incidents")
public class QualityIncident {

    @Id
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
    private Boolean resolved;
    private LocalDateTime resolutionDate;
    private String reportedByUserId;
    private String resolvedByUserId;
    private LocalDateTime createdAt = LocalDateTime.now();
}
