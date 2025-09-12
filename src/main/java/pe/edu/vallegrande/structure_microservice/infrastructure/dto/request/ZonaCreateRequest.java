package pe.edu.vallegrande.structure_microservice.infrastructure.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZonaCreateRequest {
    private String nombre;
    private String descripcion;
    private String sedeId;
}
