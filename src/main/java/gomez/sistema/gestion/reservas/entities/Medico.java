package gomez.sistema.gestion.reservas.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class Medico {

    private Integer id;
    private String nombre;
    private Especialidad especialidad;
    private int telefono;
    private LocalTime horarioInicio;
    private LocalTime horarioFin;
    private static int ultimoId;

    public Medico() {
        this.id =++ ultimoId;
    }

    public Medico(Especialidad especialidad, LocalTime horarioInicio,LocalTime horarioFin, String nombre, int telefono) {
        this();
        this.especialidad = especialidad;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public LocalTime getHorarioFin() {
        return LocalTime.of(horarioFin.getHour(), horarioFin.getMinute());
    }
    public LocalTime getHorarioInicio() {
        return LocalTime.of(horarioInicio.getHour(), horarioInicio.getMinute());
    }

    public void setHorario(LocalTime horarioInicio, LocalTime horarioFin) {
        this.horarioInicio = LocalTime.of(horarioInicio.getHour(), horarioFin.getMinute());
        this.horarioFin = LocalTime.of(horarioFin.getHour(), horarioFin.getMinute());
    }

    @Override
    public String toString() {
        return "\nNombre: " + nombre +
                "\nEspecialidad: " + especialidad +
                "\nTelefono: " + telefono +
                "\nHorario: " + horarioInicio + '-' + horarioFin;
    }

}
