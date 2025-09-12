package pe.edu.vallegrande.structure_microservice.infrastructure.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.request.subrequest.ActualizacionRequest;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.request.subrequest.CalleAfectadaRequest;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.request.subrequest.MaterialRequeridoRequest;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class IncidenciaDistribucionRequest {

    @NotBlank
    private String tipoIncidencia;

    @Size(min = 10, max = 500)
    private String descripcion;

    @NotBlank
    private String zonaId;

    @NotBlank
    private String zonaNombre;

    @NotEmpty
    private List<CalleAfectadaRequest> callesAfectadas;

    @NotNull
    private LocalDateTime fechaInicio;

    private LocalDateTime fechaEstimadaSolucion;

    private LocalDateTime fechaSolucionReal;

    @NotBlank
    private String estado;

    @NotBlank
    private String prioridad;

    @NotBlank
    private String reportadoPor;

    @NotBlank
    private String asignadoA;

    private List<MaterialRequeridoRequest> materialesRequeridos;

    @Size(max = 300)
    private String observaciones;

    private List<ActualizacionRequest> actualizaciones;

    private boolean notificado;
}
