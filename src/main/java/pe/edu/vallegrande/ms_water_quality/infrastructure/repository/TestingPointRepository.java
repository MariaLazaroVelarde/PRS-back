package pe.edu.vallegrande.ms_water_quality.infrastructure.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.ms_water_quality.domain.models.TestingPoint;
import reactor.core.publisher.Flux;

@Repository
public interface TestingPointRepository extends ReactiveMongoRepository<TestingPoint, String> {

    Flux<TestingPoint> findAllByStatus(String status);
    
    // Puedes agregar más métodos si necesitas búsquedas por zoneId, organizationId, etc.
    Flux<TestingPoint> findAllByZoneId(String zoneId);

    Flux<TestingPoint> findAllByOrganizationId(String organizationId);

}
