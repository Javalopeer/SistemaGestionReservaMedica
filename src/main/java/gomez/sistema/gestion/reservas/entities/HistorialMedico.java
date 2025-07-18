package gomez.sistema.gestion.reservas.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HistorialMedico {

    private Integer idHistorialMedico;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;
    private Cita cita;
    private Medico medico;
    private static int ultimoId;

    public HistorialMedico() {
        this.idHistorialMedico =++ ultimoId;
    }

    public HistorialMedico(Cita cita, String diagnostico, Integer idHistorialMedico, Medico medico, String observaciones, String tratamiento) {
        this();
        this.cita = cita;
        this.diagnostico = diagnostico;
        this.idHistorialMedico = idHistorialMedico;
        this.medico = medico;
        this.observaciones = observaciones;
        this.tratamiento = tratamiento;
    }

}
