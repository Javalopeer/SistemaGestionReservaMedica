package gomez.sistema.gestion.reservas.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;
import java.util.Date;

@Data
@AllArgsConstructor
public class Cita {

    private Integer idCita;
    private Date fecha;
    private LocalTime hora;
    private Paciente paciente;
    private Medico medico;
    private static int ultimoId;

    public Cita() {
        this.idCita =++ ultimoId;
    }

    public Cita(Date fecha, LocalTime hora, Medico medico, Paciente paciente) {
        this();
        this.fecha = fecha;
        this.hora = hora;
        this.medico = medico;
        this.paciente = paciente;
    }
}
