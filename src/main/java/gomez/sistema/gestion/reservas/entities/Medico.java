package gomez.sistema.gestion.reservas.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class Medico {

    private Integer id;
    private String nombre;
    private String apellido;
    private Especialidad especialidad;
    private int telefono;
    private LocalTime horarioInicio;
    private LocalTime horarioFin;
    private static int ultimoId;

    public Medico() {
        this.id =++ ultimoId;
    }

    public Medico(Especialidad especialidad, LocalTime horarioInicio,LocalTime horarioFin, String nombre, String apellido, int telefono) {
        this();
        this.especialidad = especialidad;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }

    public Medico(String nombre, String apellido) {
        this();
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Medico(String nombre, String apellido, Especialidad especialidad, LocalTime horarioInicio,LocalTime horarioFin) {
        this();
        this.especialidad = especialidad;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
        this.nombre = nombre;
        this.apellido = apellido;
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

    public String getNombreCompleto(){
        return nombre + " " + apellido;
    }

    @Override
    public String toString() {
        return "\nNombre: " + nombre + apellido +
                "\nEspecialidad: " + especialidad +
                "\nTelefono: " + telefono +
                "\nHorario: " + horarioInicio + '-' + horarioFin;
    }

}
