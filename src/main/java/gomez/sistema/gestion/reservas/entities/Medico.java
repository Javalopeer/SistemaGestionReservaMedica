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
    private String telefono;
    private LocalTime horarioInicio;
    private LocalTime horarioFin;
    private static int ultimoId;

    public Medico() {
        this.id =++ ultimoId;
    }

    public Medico(Especialidad especialidad, LocalTime horarioInicio,LocalTime horarioFin, String nombre, String apellido, String telefono) {
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

    public Medico(Integer id, String nombre, String apellido, Especialidad especialidad){
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.especialidad = especialidad;
    }

    public Medico(String nombre, String apellido, Especialidad especialidad, LocalTime horarioInicio,LocalTime horarioFin) {
        this();
        this.especialidad = especialidad;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
        this.nombre = nombre;
        this.apellido = apellido;
    }


    public LocalTime getHorarioInicio() {
        return horarioInicio != null ? LocalTime.of(horarioInicio.getHour(), horarioInicio.getMinute()) : null;
    }

    public LocalTime getHorarioFin() {
        return horarioFin != null ? LocalTime.of(horarioFin.getHour(), horarioFin.getMinute()) : null;
    }

    public void setHorario(LocalTime horarioInicio, LocalTime horarioFin) {
        this.horarioInicio = LocalTime.of(horarioInicio.getHour(), horarioFin.getMinute());
        this.horarioFin = LocalTime.of(horarioFin.getHour(), horarioFin.getMinute());
    }

    public String getNombreCompleto(){
        return nombre + " " + apellido;
    }

    public String getApellido(){
        return apellido != null ? apellido : "";
    }

    public String getTelefono(){
        return String.valueOf(telefono);
    }

    @Override
    public String toString() {
        return "\nNombre: " + nombre + apellido +
                "\nEspecialidad: " + especialidad +
                "\nTelefono: " + telefono +
                "\nHorario: " + horarioInicio + '-' + horarioFin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medico medico = (Medico) o;
        return id != null && id.equals(medico.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
