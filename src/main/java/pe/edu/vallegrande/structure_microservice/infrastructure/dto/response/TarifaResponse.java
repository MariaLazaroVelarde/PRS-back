package pe.edu.vallegrande.structure_microservice.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarifaResponse {
    private String id; 
    private String zonaId; 
    private double monto; 
    private String descripcion; 
    private String tipoTarifa; 
    private Date fechaInicioVigencia; 
    private Date fechaFinVigencia; 
    private String status; 
    private LocalDateTime fechaRegistro; 
    private String zonaNombre; 
}
