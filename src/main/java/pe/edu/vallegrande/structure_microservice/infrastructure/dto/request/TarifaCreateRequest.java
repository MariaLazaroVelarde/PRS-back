package pe.edu.vallegrande.structure_microservice.infrastructure.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarifaCreateRequest {
    private String zonaId; 
    private double monto; 
    private String descripcion; 
    private String tipoTarifa; 
    private Date fechaInicioVigencia; 
    private Date fechaFinVigencia; 
    private String zonaNombre; 
}
