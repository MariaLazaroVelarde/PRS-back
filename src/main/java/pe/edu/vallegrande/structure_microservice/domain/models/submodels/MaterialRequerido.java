package pe.edu.vallegrande.structure_microservice.domain.models.submodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaterialRequerido {
    private String nombre;
    private Integer cantidad;
    private String unidad;
}

