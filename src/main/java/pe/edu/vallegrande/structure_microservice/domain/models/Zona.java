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
@Document(collection = "zonas")
public class Zona {
    @Id
    private String zonaId;
    private String nombre;
    private String descripcion;
    private String sedeId;
    private String status = "ACTIVE";
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}
