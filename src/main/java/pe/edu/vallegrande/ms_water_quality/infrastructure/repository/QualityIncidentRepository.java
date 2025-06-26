package pe.edu.vallegrande.ms_water_quality.infrastructure.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.ms_water_quality.domain.models.QualityIncident;
import reactor.core.publisher.Flux;

@Repository
public interface QualityIncidentRepository extends ReactiveMongoRepository<QualityIncident, String> {
    Flux<QualityIncident> findAllByResolved(Boolean resolved);
}
