package pe.edu.vallegrande.structure_microservice.infrastructure.dto.request.subrequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActualizacionRequest {
    @NotNull
    private LocalDateTime fecha;
    @NotBlank
    private String estado;
    @NotBlank
    private String descripcion;
    @NotBlank
    private String usuarioId;
}