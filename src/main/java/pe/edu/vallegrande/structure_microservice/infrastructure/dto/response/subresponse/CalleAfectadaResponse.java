package pe.edu.vallegrande.structure_microservice.infrastructure.dto.response.subresponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalleAfectadaResponse {
    private String calleId;
    private String calleNombre;
}