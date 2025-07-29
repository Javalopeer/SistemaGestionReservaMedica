package gomez.sistema.gestion.reservas.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class Cita {

    private Integer idCita;
    private LocalDate fecha;
    private LocalTime hora;
    private Paciente paciente;
    private Medico medico;
    private static int ultimoId;

    public Cita() {
        this.idCita =++ ultimoId;
    }

    public Cita(LocalDate fecha, LocalTime hora, Medico medico, Paciente paciente) {
        this();
        this.fecha = fecha;
        this.hora = hora;
        this.medico = medico;
        this.paciente = paciente;
    }

}
