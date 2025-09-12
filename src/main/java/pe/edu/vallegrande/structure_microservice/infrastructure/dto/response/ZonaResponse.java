package pe.edu.vallegrande.structure_microservice.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZonaResponse {
    private String zonaId;
    private String nombre;
    private String descripcion;
    private String sedeId;
    private String status;
    private LocalDateTime fechaRegistro;
}
