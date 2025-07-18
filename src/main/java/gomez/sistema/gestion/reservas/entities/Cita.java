package gomez.sistema.gestion.reservas.entities;

import java.time.LocalTime;
import java.util.Date;

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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}
