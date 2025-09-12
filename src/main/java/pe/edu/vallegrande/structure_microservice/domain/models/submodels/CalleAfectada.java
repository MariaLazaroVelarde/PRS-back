package pe.edu.vallegrande.structure_microservice.domain.models.submodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalleAfectada {
    private String calleId;
    private String calleNombre;
}

