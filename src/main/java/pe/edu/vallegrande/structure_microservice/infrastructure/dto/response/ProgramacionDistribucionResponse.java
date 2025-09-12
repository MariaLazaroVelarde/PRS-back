package pe.edu.vallegrande.structure_microservice.infrastructure.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ProgramacionDistribucionResponse {

    private String id;
    private String calleNombre;
    private String zonaNombre;
    private String horaInicio;
    private String horaFin;
    private boolean esDiario;
    private boolean estado;
    private String observaciones;
    private LocalDate fechaRegistro;
    private String responsableId;
}
