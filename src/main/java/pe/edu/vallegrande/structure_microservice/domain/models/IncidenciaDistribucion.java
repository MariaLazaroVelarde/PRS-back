package pe.edu.vallegrande.structure_microservice.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "incidencias_distribucion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidenciaDistribucion {
    @Id
    private String id;
    private String tipoIncidencia;
    private String descripcion;
    private String zonaId;
    private String zonaNombre;
    private List<CalleAfectada> callesAfectadas;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaEstimadaSolucion;
    private LocalDateTime fechaSolucionReal;
    private String estado;
    private String prioridad;
    private String reportadoPor;
    private String asignadoA;
    private List<MaterialRequerido> materialesRequeridos;
    private String observaciones;
    private List<Actualizacion> actualizaciones;
    private boolean notificado;
    private LocalDateTime fechaRegistro;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CalleAfectada {
        private String calleId;
        private String calleNombre;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MaterialRequerido {
        private String nombre;
        private int cantidad;
        private String unidad;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Actualizacion {
        private LocalDateTime fecha;
        private String estado;
        private String descripcion;
        private String usuarioId;
    }
}
