package pe.edu.vallegrande.ms_water_quality.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.ms_water_quality.application.services.QualityIncidentService;
import pe.edu.vallegrande.ms_water_quality.domain.models.QualityIncident;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.QualityIncidentCreateRequest;
import pe.edu.vallegrande.ms_water_quality.infrastructure.exception.CustomException;
import pe.edu.vallegrande.ms_water_quality.infrastructure.repository.QualityIncidentRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class QualityIncidentServiceImpl implements QualityIncidentService {

    private final QualityIncidentRepository repository;

    @Override
    public Flux<QualityIncident> getAll() {
        return repository.findAll();
    }

    @Override
    public Flux<QualityIncident> getResolved() {
        return repository.findAllByResolved(true);
    }

    @Override
    public Flux<QualityIncident> getUnresolved() {
        return repository.findAllByResolved(false);
    }

    @Override
    public Mono<QualityIncident> getById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Incident not found",
                        "No incident found with ID " + id
                )));
    }

    @Override
    public Mono<QualityIncident> save(QualityIncidentCreateRequest request) {
        QualityIncident incident = new QualityIncident();
        incident.setOrganizationId(request.getOrganizationId());
        incident.setIncidentCode(request.getIncidentCode());
        incident.setIncidentType(request.getIncidentType());
        incident.setTestingPointId(request.getTestingPointId());
        incident.setDetectionDate(request.getDetectionDate());
        incident.setSeverity(request.getSeverity());
        incident.setDescription(request.getDescription());
        incident.setAffectedZones(request.getAffectedZones());
        incident.setImmediateActions(request.getImmediateActions());
        incident.setCorrectiveActions(request.getCorrectiveActions());
        incident.setResolved(request.getResolved());
        incident.setResolutionDate(request.getResolutionDate());
        incident.setReportedByUserId(request.getReportedByUserId());
        incident.setResolvedByUserId(request.getResolvedByUserId());
        incident.setCreatedAt(LocalDateTime.now());

        return repository.save(incident);
    }

    @Override
    public Mono<QualityIncident> update(String id, QualityIncident updatedIncident) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Incident not found",
                        "No incident found with ID " + id
                )))
                .flatMap(existing -> {
                    updatedIncident.setId(existing.getId());
                    updatedIncident.setCreatedAt(existing.getCreatedAt());
                    return repository.save(updatedIncident);
                });
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Incident not found",
                        "No incident found with ID " + id
                )))
                .flatMap(repository::delete);
    }
}
