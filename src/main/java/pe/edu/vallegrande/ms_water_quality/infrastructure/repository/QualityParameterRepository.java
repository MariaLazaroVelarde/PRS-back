package pe.edu.vallegrande.ms_water_quality.infrastructure.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.ms_water_quality.domain.models.QualityParameter;
import reactor.core.publisher.Flux;

@Repository
public interface QualityParameterRepository extends ReactiveMongoRepository<QualityParameter, String> {
    Flux<QualityParameter> findByStatus(String status);
}