package pe.edu.vallegrande.structure_microservice.infrastructure.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProgramacionDistribucionRequest {

    @NotBlank
    private String calleId;

    @NotBlank
    private String zonaId;

    @NotBlank
    private String horaInicio;

    @NotBlank
    private String horaFin;

    private boolean esDiario;

    private boolean estado = true;

    @Size(max = 250)
    private String observaciones;

    @NotBlank
    private String responsableId;

    @NotBlank
    private String calleNombre;

    @NotBlank
    private String zonaNombre;
}


