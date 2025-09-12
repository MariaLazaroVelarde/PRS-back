package pe.edu.vallegrande.structure_microservice.infrastructure.dto.response.subresponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaterialRequeridoResponse {
    private String nombre;
    private Integer cantidad;
    private String unidad;
}