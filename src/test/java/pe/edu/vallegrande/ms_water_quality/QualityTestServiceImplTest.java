package pe.edu.vallegrande.ms_water_quality;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.edu.vallegrande.ms_water_quality.application.services.impl.QualityTestServiceImpl;
import pe.edu.vallegrande.ms_water_quality.domain.models.QualityTest;
import pe.edu.vallegrande.ms_water_quality.infrastructure.dto.request.QualityTestCreateRequest;
import pe.edu.vallegrande.ms_water_quality.infrastructure.repository.QualityTestRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QualityTestServiceImplTest {

    @Mock
    private QualityTestRepository repository;

    @InjectMocks
    private QualityTestServiceImpl service;

    private QualityTest sampleTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleTest = new QualityTest();
        sampleTest.setId("TST001");
        sampleTest.setTestCode("ANL001");
        sampleTest.setOrganizationId("ORG1");
        sampleTest.setTestingPointId("TP01");
        sampleTest.setStatus("COMPLETED");
        sampleTest.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testGetAll() {
        when(repository.findAll()).thenReturn(Flux.just(sampleTest));

        StepVerifier.create(service.getAll())
                .expectNext(sampleTest)
                .verifyComplete();
    }

    @Test
    void testGetByIdFound() {
        when(repository.findById("TST001")).thenReturn(Mono.just(sampleTest));

        StepVerifier.create(service.getById("TST001"))
                .expectNextMatches(qt -> qt.getTestCode().equals("ANL001"))
                .verifyComplete();
    }

    @Test
    void testSaveValidRequest() {
        QualityTestCreateRequest request = new QualityTestCreateRequest();
        request.setOrganizationId("ORG1");
        request.setTestingPointId("TP01");
        request.setTestDate(LocalDateTime.now());
        request.setResults(Collections.emptyList());

        when(repository.findAll()).thenReturn(Flux.empty()); // generateNextCode
        when(repository.save(any(QualityTest.class))).thenReturn(Mono.just(sampleTest));

        StepVerifier.create(service.save(request))
                .expectNextMatches(saved -> saved.getTestCode().equals("ANL001"))
                .verifyComplete();
    }

    @Test
    void testDeletePhysically() {
        when(repository.findById("TST001")).thenReturn(Mono.just(sampleTest));
        when(repository.delete(sampleTest)).thenReturn(Mono.empty());

        StepVerifier.create(service.deletePhysically("TST001"))
                .verifyComplete();
    }
}

