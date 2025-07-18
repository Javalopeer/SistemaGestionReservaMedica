package gomez.sistema.gestion.reservas.entities;

public class Paciente {

    private Float cedula;
    private String nombre;
    private String apellido;
    private String telefono;

    public Paciente() {
    }

    public Paciente(String apellido, String nombre, String telefono) {
        this.apellido = apellido;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Float getCedula() {
        return cedula;
    }

    public void setCedula(Float cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
