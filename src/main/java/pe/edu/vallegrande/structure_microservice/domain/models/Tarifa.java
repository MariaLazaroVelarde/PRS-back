package pe.edu.vallegrande.structure_microservice.domain.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tarifas")
public class Tarifa {
    @Id
    private String tarifaId;
    private String zonaId;
    private double monto;
    private String descripcion;
    private String tipoTarifa;
    private Date fechaInicioVigencia;
    private Date fechaFinVigencia;
    private String status = "ACTIVE";
    private LocalDateTime fechaRegistro = LocalDateTime.now();
    private String zonaNombre;
}