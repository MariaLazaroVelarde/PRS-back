package pe.edu.vallegrande.structure_microservice.infrastructure.dto.request.subrequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MaterialRequeridoRequest {
    @NotBlank
    private String nombre;
    @NotNull
    private Integer cantidad;
    @NotBlank
    private String unidad;
}

