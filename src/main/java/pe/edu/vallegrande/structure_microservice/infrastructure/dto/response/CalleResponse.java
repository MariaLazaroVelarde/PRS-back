package pe.edu.vallegrande.structure_microservice.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalleResponse {
    private String calleId;
    private String nombre;
    private String zonaId;
    private String zonaNombre;
    private boolean estado; 
    private LocalDateTime fechaRegistro;
}
