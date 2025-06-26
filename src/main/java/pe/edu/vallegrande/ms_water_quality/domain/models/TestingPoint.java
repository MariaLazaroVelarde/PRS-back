package pe.edu.vallegrande.ms_water_quality.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "testing_points")
public class TestingPoint {

    @Id
    private String id;

    @Field("organization_id")
    private String organizationId;

    @Field("point_code")
    private String pointCode;

    @Field("point_name")
    private String pointName;

    @Field("point_type")
    private String pointType; // RESERVORIO, RED_DISTRIBUCION, DOMICILIO

    @Field("zone_id")
    private String zoneId;

    @Field("location_description")
    private String locationDescription;

    private Coordinates coordinates;

    private String status = "ACTIVE";

    @Field("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Clase interna para coordenadas
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coordinates {
        private double latitude;
        private double longitude;
    }
}
