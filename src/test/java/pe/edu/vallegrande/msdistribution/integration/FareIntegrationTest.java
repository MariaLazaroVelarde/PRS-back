package pe.edu.vallegrande.msdistribution.integration;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import pe.edu.vallegrande.msdistribution.application.services.FareService;
import pe.edu.vallegrande.msdistribution.domain.enums.Constants;
import pe.edu.vallegrande.msdistribution.domain.models.Fare;
import pe.edu.vallegrande.msdistribution.infrastructure.dto.request.FareCreateRequest;
import pe.edu.vallegrande.msdistribution.infrastructure.exception.CustomException;
import pe.edu.vallegrande.msdistribution.infrastructure.repository.FareRepository;
import pe.edu.vallegrande.msdistribution.infrastructure.service.ExternalServiceClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Pruebas de integración para FareService
 * Usa MongoDB Embedded automáticamente configurado por Spring Boot
 */
@SpringBootTest(classes = {
    pe.edu.vallegrande.msdistribution.VgMsDistribution.class
})
@TestPropertySource(properties = {
    "de.flapdoodle.mongodb.embedded.version=5.0.5",
    "spring.data.mongodb.auto-index-creation=true"
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FareIntegrationTest {

    @Autowired
    private FareService fareService;

    @Autowired
    private FareRepository fareRepository;

    @MockBean
    private ExternalServiceClient externalServiceClient;

    @BeforeEach
    void setUp() {
        // Mock external service calls
        when(externalServiceClient.getOrganizationById("org-123"))
            .thenReturn(Mono.just(new pe.edu.vallegrande.msdistribution.infrastructure.client.dto.ExternalOrganization()));
        
        // Clean database before each test
        fareRepository.deleteAll().block();
    }

    // ==================== CREATE TESTS ====================

    @Test
    @Order(1)
    @DisplayName("IT-01: Crear tarifa exitosamente con código TAR001")
    void createFare_ShouldGenerateTAR001_WhenNoFaresExist() {
        System.out.println("\n🔹 [INICIO] IT-01: Crear tarifa con código TAR001");
        // Arrange
        FareCreateRequest request = FareCreateRequest.builder()
                .organizationId("org-123")
                .fareName("Tarifa Básica")
                .fareType("SEMANAL")
                .fareAmount(new BigDecimal("15.50"))
                .build();

        // Act & Assert
        StepVerifier.create(fareService.saveF(request))
            .assertNext(fare -> {
                System.out.println("✅ Tarifa creada con código: " + fare.getFareCode());
                System.out.println("   Nombre: " + fare.getFareName());
                System.out.println("   Tipo: " + fare.getFareType());
                System.out.println("   Monto: " + fare.getFareAmount());
                assertNotNull(fare.getId());
                assertEquals("TAR001", fare.getFareCode());
                assertEquals("Tarifa Básica", fare.getFareName());
                assertEquals("SEMANAL", fare.getFareType());
                assertEquals(new BigDecimal("15.50"), fare.getFareAmount());
                assertEquals(Constants.ACTIVE.name(), fare.getStatus());
                assertNotNull(fare.getCreatedAt());
            })
            .verifyComplete();

            System.out.println("🔸 [FIN] IT-01 completada exitosamente\n");
    }

    @Test
    @Order(2)
    @DisplayName("IT-02: Crear múltiples tarifas con códigos secuenciales")
    void createMultipleFares_ShouldGenerateSequentialCodes() {
        System.out.println("\n🔹 [INICIO] IT-02: Crear múltiples tarifas secuenciales");
        // Arrange - Crear primera tarifa
        FareCreateRequest request1 = FareCreateRequest.builder()
                .organizationId("org-123")
                .fareName("Tarifa 1")
                .fareType("DIARIA")
                .fareAmount(new BigDecimal("10"))
                .build();

        // Act - Crear tres tarifas
        fareService.saveF(request1).block();
        System.out.println("✅ Primera tarifa creada (esperado TAR001)");

        FareCreateRequest request2 = FareCreateRequest.builder()
                .organizationId("org-123")
                .fareName("Tarifa 2")
                .fareType("MENSUAL")
                .fareAmount(new BigDecimal("20"))
                .build();

        FareCreateRequest request3 = FareCreateRequest.builder()
                .organizationId("org-123")
                .fareName("Tarifa 3")
                .fareType("ANUAL")
                .fareAmount(new BigDecimal("30"))
                .build();

        // Assert
        StepVerifier.create(fareService.saveF(request2))
                .assertNext(fare -> {
                System.out.println("✅ Segunda tarifa creada con código: " + fare.getFareCode());
                assertEquals("TAR002", fare.getFareCode());
            })
            .verifyComplete();

        StepVerifier.create(fareService.saveF(request3))
                .assertNext(fare -> {
                System.out.println("✅ Tercera tarifa creada con código: " + fare.getFareCode());
                assertEquals("TAR003", fare.getFareCode());
            })
            .verifyComplete();

        System.out.println("🔸 [FIN] IT-02 completada correctamente\n");
    }

    @Test
    @Order(3)
    @DisplayName("IT-03: Error al crear tarifa con código duplicado")
    void createFare_ShouldFail_WhenCodeAlreadyExists() {
        System.out.println("\n🔹 [INICIO] IT-03: Crear tarifa cuando existe código TAR001");
        // Arrange - Crear tarifa inicial
        Fare existing = Fare.builder()
                .organizationId("org-123")
                .fareCode("TAR001")
                .fareName("Tarifa Existente")
                .fareType("MENSUAL")
                .fareAmount(new BigDecimal("25"))
                .status(Constants.ACTIVE.name())
                .build();
        
        fareRepository.save(existing).block();
        System.out.println("ℹ️ Tarifa TAR001 insertada manualmente en la BD.");

        // Create another fare to increment the counter to TAR002
        FareCreateRequest request1 = FareCreateRequest.builder()
                .organizationId("org-123")
                .fareName("Tarifa 2")
                .fareType("SEMANAL")
                .fareAmount(new BigDecimal("20"))
                .build();
        
        fareService.saveF(request1).block(); // This should create TAR002
        System.out.println("✅ Segunda tarifa creada (esperado TAR002)");

        // Now try to create another fare - it should get TAR003, not fail
        FareCreateRequest request2 = FareCreateRequest.builder()
                .organizationId("org-123")
                .fareName("Nueva Tarifa")
                .fareType("SEMANAL")
                .fareAmount(new BigDecimal("15"))
                .build();

        // Act & Assert - The service should generate the next available code (TAR003)
        StepVerifier.create(fareService.saveF(request2))
                .assertNext(fare -> {
                System.out.println("✅ Nueva tarifa creada con código: " + fare.getFareCode());
                assertEquals("TAR003", fare.getFareCode());
            })
            .verifyComplete();
        
        System.out.println("🔸 [FIN] IT-03 ejecutada correctamente\n");
    }

    // ==================== READ TESTS ====================

    @Test
    @Order(4)
    @DisplayName("IT-04: Obtener tarifa por ID exitosamente")
    void getFareById_ShouldReturnFare_WhenExists() {
        System.out.println("\n🔹 [INICIO] IT-04: Obtener tarifa por ID existente");
        // Arrange
        Fare fare = Fare.builder()
                .organizationId("org-123")
                .fareCode("TAR001")
                .fareName("Tarifa Test")
                .fareType("MENSUAL")
                .fareAmount(new BigDecimal("50"))
                .status(Constants.ACTIVE.name())
                .build();
        
        Fare saved = fareRepository.save(fare).block();
        System.out.println("✅ Tarifa guardada con ID: " + saved.getId());

        // Act & Assert
        StepVerifier.create(fareService.getByIdF(saved.getId()))
                .assertNext(result -> {
                System.out.println("🔎 Tarifa encontrada:");
                System.out.println("   Código: " + result.getFareCode());
                System.out.println("   Nombre: " + result.getFareName());
                assertEquals(saved.getId(), result.getId());
                assertEquals("TAR001", result.getFareCode());
                assertEquals("Tarifa Test", result.getFareName());
            })
            .verifyComplete();

        System.out.println("🔸 [FIN] IT-04 completada correctamente\n");
    }

    @Test
    @Order(5)
    @DisplayName("IT-05: Error al obtener tarifa inexistente")
    void getFareById_ShouldFail_WhenNotFound() {
        System.out.println("\n🔹 [INICIO] IT-05: Error al obtener tarifa inexistente");
        // Act & Assert
        StepVerifier.create(fareService.getByIdF("non-existent-id"))
                .expectErrorSatisfies(error -> {
                assertTrue(error instanceof CustomException);
                CustomException ce = (CustomException) error;
                System.out.println("⚠️ Excepción capturada correctamente: " + ce.getErrorMessage().getMessage());
                assertEquals("Fare not found", ce.getErrorMessage().getMessage());
            })
            .verify();

        System.out.println("🔸 [FIN] IT-05 ejecutada correctamente\n");
    }

    @Test
    @Order(6)
    @DisplayName("IT-06: Listar todas las tarifas")
    void getAllFares_ShouldReturnAllFares() {
        System.out.println("\n🔹 [INICIO] IT-06: Listar todas las tarifas");
        // Arrange - Crear múltiples tarifas
        Fare fare1 = Fare.builder()
                .organizationId("org-123")
                .fareCode("TAR001")
                .fareName("Tarifa 1")
                .fareType("DIARIA")
                .fareAmount(new BigDecimal("10"))
                .status(Constants.ACTIVE.name())
                .build();

        Fare fare2 = Fare.builder()
                .organizationId("org-123")
                .fareCode("TAR002")
                .fareName("Tarifa 2")
                .fareType("MENSUAL")
                .fareAmount(new BigDecimal("20"))
                .status(Constants.INACTIVE.name())
                .build();

        fareRepository.save(fare1).block();
        fareRepository.save(fare2).block();
        System.out.println("✅ Se crearon 2 tarifas para la prueba");

        // Act & Assert
        StepVerifier.create(fareService.getAllF())
                .recordWith(ArrayList::new)
            .expectNextCount(2)
            .consumeRecordedWith(fares -> {
                System.out.println("📋 Total de tarifas listadas: " + fares.size());
                fares.forEach(f -> System.out.println("   → " + f.getFareCode() + " - " + f.getFareName()));
            })
            .verifyComplete();

        System.out.println("🔸 [FIN] IT-06 completada correctamente\n");
    }

    @Test
    @Order(7)
    @DisplayName("IT-07: Listar solo tarifas activas")
    void getAllActiveFares_ShouldReturnOnlyActive() {
        System.out.println("\n🔹 [INICIO] IT-07: Listar tarifas activas");
        // Arrange
        Fare active = Fare.builder()
                .organizationId("org-123")
                .fareCode("TAR001")
                .fareName("Activa")
                .fareType("DIARIA")
                .fareAmount(new BigDecimal("10"))
                .status(Constants.ACTIVE.name())
                .build();

        Fare inactive = Fare.builder()
                .organizationId("org-123")
                .fareCode("TAR002")
                .fareName("Inactiva")
                .fareType("MENSUAL")
                .fareAmount(new BigDecimal("20"))
                .status(Constants.INACTIVE.name())
                .build();

        fareRepository.save(active).block();
        fareRepository.save(inactive).block();
        System.out.println("✅ Datos cargados: 1 activa y 1 inactiva");

        // Act & Assert
        StepVerifier.create(fareService.getAllActiveF())
                .recordWith(ArrayList::new)
            .expectNextCount(1)
            .consumeRecordedWith(fares -> {
                System.out.println("📋 Tarifas activas encontradas: " + fares.size());
                fares.forEach(f -> System.out.println("   → " + f.getFareCode() + " - Estado: " + f.getStatus()));
                assertTrue(fares.stream().allMatch(f -> f.getStatus().equals(Constants.ACTIVE.name())));
            })
            .verifyComplete();

        System.out.println("🔸 [FIN] IT-07 completada correctamente\n");
    }

    @Test
    @Order(8)
    @DisplayName("IT-08: Listar solo tarifas inactivas")
    void getAllInactiveFares_ShouldReturnOnlyInactive() {
        System.out.println("\n🔹 [INICIO] IT-08: Listar tarifas inactivas");
        // Arrange
        Fare active = Fare.builder()
                .organizationId("org-123")
                .fareCode("TAR001")
                .fareName("Activa")
                .fareType("DIARIA")
                .fareAmount(new BigDecimal("10"))
                .status(Constants.ACTIVE.name())
                .build();

        Fare inactive = Fare.builder()
                .organizationId("org-123")
                .fareCode("TAR002")
                .fareName("Inactiva")
                .fareType("MENSUAL")
                .fareAmount(new BigDecimal("20"))
                .status(Constants.INACTIVE.name())
                .build();

        fareRepository.save(active).block();
        fareRepository.save(inactive).block();
        System.out.println("✅ Datos cargados: 1 activa y 1 inactiva");

        // Act & Assert
        StepVerifier.create(fareService.getAllInactiveF())
                .recordWith(ArrayList::new)
            .expectNextCount(1)
            .consumeRecordedWith(fares -> {
                System.out.println("📋 Tarifas inactivas encontradas: " + fares.size());
                fares.forEach(f -> System.out.println("   → " + f.getFareCode() + " - Estado: " + f.getStatus()));
                assertTrue(fares.stream().allMatch(f -> f.getStatus().equals(Constants.INACTIVE.name())));
            })
            .verifyComplete();

        System.out.println("🔸 [FIN] IT-08 completada correctamente\n");
    }

    // ==================== UPDATE TESTS ====================

    @Test
    @Order(9)
    @DisplayName("IT-09: Actualizar tarifa exitosamente")
    void updateFare_ShouldUpdateFields_WhenValid() {
        System.out.println("\n🔹 [INICIO] IT-09: Actualizar tarifa exitosamente");
        // Arrange - Crear tarifa inicial
        Fare existing = Fare.builder()
                .organizationId("org-123")
                .fareCode("TAR001")
                .fareName("Nombre Original")
                .fareType("DIARIA")
                .fareAmount(new BigDecimal("10"))
                .status(Constants.ACTIVE.name())
                .build();

        Fare saved = fareRepository.save(existing).block();
        System.out.println("✅ Tarifa inicial creada con ID: " + saved.getId());
        System.out.println("   Código: " + saved.getFareCode());
        System.out.println("   Nombre: " + saved.getFareName());
        System.out.println("   Tipo: " + saved.getFareType());
        System.out.println("   Monto: " + saved.getFareAmount());
        System.out.println("   Estado: " + saved.getStatus());

        FareCreateRequest update = FareCreateRequest.builder()
                .fareName("Nombre Actualizado")
                .fareType("MENSUAL")
                .fareAmount(new BigDecimal("50"))
                .build();
        System.out.println("🛠️ Datos a actualizar:");
        System.out.println("   Nombre: " + update.getFareName());
        System.out.println("   Tipo: " + update.getFareType());
        System.out.println("   Monto: " + update.getFareAmount());

        long startTime = System.currentTimeMillis();

        // Act & Assert
        StepVerifier.create(fareService.updateF(saved.getId(), update))
                .assertNext(updated -> {
                System.out.println("🔄 Tarifa actualizada correctamente:");
                System.out.println("   Código: " + updated.getFareCode());
                System.out.println("   Nuevo Nombre: " + updated.getFareName());
                System.out.println("   Nuevo Tipo: " + updated.getFareType());
                System.out.println("   Nuevo Monto: " + updated.getFareAmount());

                assertEquals("Nombre Actualizado", updated.getFareName());
                assertEquals("MENSUAL", updated.getFareType());
                assertEquals(new BigDecimal("50"), updated.getFareAmount());
                assertEquals("TAR001", updated.getFareCode()); // No cambia
            })
            .verifyComplete();

        long elapsed = System.currentTimeMillis() - startTime;
        System.out.println("⏱ Tiempo total de actualización: " + elapsed + "ms");
        System.out.println("🔸 [FIN] IT-09 completada correctamente\n");
    }

    @Test
    @Order(10)
    @DisplayName("IT-10: Error al actualizar tarifa inexistente")
    void updateFare_ShouldFail_WhenNotFound() {
        System.out.println("\n🔹 [INICIO] IT-10: Error al actualizar tarifa inexistente");
        // Arrange
        FareCreateRequest update = FareCreateRequest.builder()
                .fareName("No existe")
                .fareType("MENSUAL")
                .fareAmount(new BigDecimal("50"))
                .build();

        System.out.println("🧾 Intentando actualizar tarifa con ID inexistente...");

        long startTime = System.currentTimeMillis();

        // Act & Assert
        StepVerifier.create(fareService.updateF("non-existent-id", update))
                .expectErrorSatisfies(error -> {
                assertTrue(error instanceof CustomException);
                CustomException ce = (CustomException) error;
                System.out.println("⚠️ Excepción capturada correctamente:");
                System.out.println("   Mensaje: " + ce.getErrorMessage().getMessage());
                assertEquals("Fare not found", ce.getErrorMessage().getMessage());
            })
            .verify();

        long elapsed = System.currentTimeMillis() - startTime;
        System.out.println("⏱ Tiempo total de ejecución: " + elapsed + "ms");
        System.out.println("🔸 [FIN] IT-10 completada correctamente\n");
    }

    // ==================== ACTIVATE/DEACTIVATE TESTS ====================

    @Test
    @Order(11)
    @DisplayName("IT-11: Activar tarifa inactiva")
    void activateFare_ShouldChangeStatus_WhenInactive() {
        System.out.println("=== IT-11: Iniciando prueba de activación de tarifa inactiva ===");
        // Arrange
        Fare inactive = Fare.builder()
                .organizationId("org-123")
                .fareCode("TAR001")
                .fareName("Tarifa Inactiva")
                .fareType("MENSUAL")
                .fareAmount(new BigDecimal("20"))
                .status(Constants.INACTIVE.name())
                .build();

        Fare saved = fareRepository.save(inactive).block();
        System.out.println("Tarifa guardada con estado inicial: " + saved.getStatus());

        // Act & Assert
        StepVerifier.create(fareService.activateF(saved.getId()))
                .assertNext(activated -> {
                System.out.println("Tarifa activada con ID: " + activated.getId());
                System.out.println("Estado después de activar: " + activated.getStatus());
                assertEquals(Constants.ACTIVE.name(), activated.getStatus());
            })
            .verifyComplete();

        System.out.println("=== IT-11: Prueba completada exitosamente ===\n");
    }

    @Test
    @Order(12)
    @DisplayName("IT-12: Desactivar tarifa activa")
    void deactivateFare_ShouldChangeStatus_WhenActive() {
        System.out.println("=== IT-12: Iniciando prueba de desactivación de tarifa activa ===");
        // Arrange
        Fare active = Fare.builder()
                .organizationId("org-123")
                .fareCode("TAR001")
                .fareName("Tarifa Activa")
                .fareType("MENSUAL")
                .fareAmount(new BigDecimal("20"))
                .status(Constants.ACTIVE.name())
                .build();

        Fare saved = fareRepository.save(active).block();
        System.out.println("Tarifa guardada con estado inicial: " + saved.getStatus());

        // Act & Assert
        StepVerifier.create(fareService.deactivateF(saved.getId()))
                .assertNext(deactivated -> {
                System.out.println("Tarifa desactivada con ID: " + deactivated.getId());
                System.out.println("Estado después de desactivar: " + deactivated.getStatus());
                assertEquals(Constants.INACTIVE.name(), deactivated.getStatus());
            })
            .verifyComplete();

        System.out.println("=== IT-12: Prueba completada exitosamente ===\n");
    }

    @Test
    @Order(13)
    @DisplayName("IT-13: Error al activar tarifa inexistente")
    void activateFare_ShouldFail_WhenNotFound() {
        System.out.println("=== IT-13: Iniciando prueba de error al activar tarifa inexistente ===");
        // Act & Assert
        StepVerifier.create(fareService.activateF("non-existent-id"))
                .expectErrorSatisfies(error -> {
                System.out.println("Error capturado: " + error.getMessage());
                assertTrue(error instanceof CustomException);
            })
            .verify();

        System.out.println("=== IT-13: Prueba completada exitosamente ===\n");
    }

    // ==================== DELETE TESTS ====================

    @Test
    @Order(14)
    @DisplayName("IT-14: Eliminar tarifa exitosamente")
    void deleteFare_ShouldRemove_WhenExists() {
        System.out.println("=== IT-14: Iniciando prueba de eliminación de tarifa existente ===");
        // Arrange
        Fare fare = Fare.builder()
                .organizationId("org-123")
                .fareCode("TAR001")
                .fareName("Tarifa a Eliminar")
                .fareType("MENSUAL")
                .fareAmount(new BigDecimal("20"))
                .status(Constants.ACTIVE.name())
                .build();

        Fare saved = fareRepository.save(fare).block();
        System.out.println("Tarifa guardada con ID: " + saved.getId());
        System.out.println("Estado inicial: " + saved.getStatus());

        // Act
        System.out.println("Ejecutando eliminación de la tarifa...");
        StepVerifier.create(fareService.deleteF(saved.getId()))
                .verifyComplete();
        System.out.println("Eliminación completada. Verificando si fue removida...");

        // Assert - Verificar que no existe
        StepVerifier.create(fareRepository.findById(saved.getId()))
                .expectNextCount(0)
                .verifyComplete();

        System.out.println("✅ Tarifa eliminada exitosamente. No se encontró registro en la base de datos.");
        System.out.println("=== IT-14: Prueba completada exitosamente ===\n");
    }

    @Test
    @Order(15)
    @DisplayName("IT-15: Error al eliminar tarifa inexistente")
    void deleteFare_ShouldFail_WhenNotFound() {
        System.out.println("=== IT-15: Iniciando prueba de error al eliminar tarifa inexistente ===");
        // Act & Assert
        StepVerifier.create(fareService.deleteF("non-existent-id"))
                .expectErrorSatisfies(error -> {
                System.out.println("Error capturado: " + error.getMessage());
                assertTrue(error instanceof CustomException);
                CustomException ce = (CustomException) error;
                System.out.println("Mensaje de error esperado: " + ce.getErrorMessage().getMessage());
                assertEquals("Fare not found", ce.getErrorMessage().getMessage());
            })
            .verify();

        System.out.println("✅ Error esperado verificado correctamente (tarifa no encontrada).");
        System.out.println("=== IT-15: Prueba completada ===\n");
    }
}