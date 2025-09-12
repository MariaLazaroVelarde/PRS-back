package pe.edu.vallegrande.structure_microservice.domain.models.submodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Actualizacion {
    private LocalDateTime fecha;
    private String estado;
    private String descripcion;
    private String usuarioId;
}

