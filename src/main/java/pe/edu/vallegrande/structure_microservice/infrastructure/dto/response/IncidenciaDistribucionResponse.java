package pe.edu.vallegrande.structure_microservice.infrastructure.dto.response;

import lombok.Builder;
import lombok.Data;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.response.subresponse.ActualizacionResponse;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.response.subresponse.CalleAfectadaResponse;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.response.subresponse.MaterialRequeridoResponse;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class IncidenciaDistribucionResponse {

    private String id;
    private String tipoIncidencia;
    private String descripcion;
    private String zonaNombre;
    private List<CalleAfectadaResponse> callesAfectadas;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaEstimadaSolucion;
    private LocalDateTime fechaSolucionReal;
    private String estado;
    private String prioridad;
    private String reportadoPor;
    private String asignadoA;
    private List<MaterialRequeridoResponse> materialesRequeridos;
    private String observaciones;
    private List<ActualizacionResponse> actualizaciones;
    private boolean notificado;
    private LocalDateTime fechaRegistro;
}
