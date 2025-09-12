package pe.edu.vallegrande.structure_microservice.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "calles")
public class Calle {
    @Id
    private String calleId;
    private String nombre;
    private String zonaId;
    private String zonaNombre;
    private boolean estado = true; // Activo por defecto
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}
