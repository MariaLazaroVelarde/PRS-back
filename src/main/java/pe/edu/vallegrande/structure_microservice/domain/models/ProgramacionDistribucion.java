package pe.edu.vallegrande.structure_microservice.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "programacion_distribucion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramacionDistribucion {
    @Id
    private String id;
    private String calleId;
    private String zonaId;
    private String horaInicio;
    private String horaFin;
    private boolean esDiario;
    private boolean estado;
    private String observaciones;
    private LocalDate fechaRegistro;
    private String responsableId;
    private String calleNombre;
    private String zonaNombre;
}
