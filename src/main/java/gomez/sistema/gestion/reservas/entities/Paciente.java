package gomez.sistema.gestion.reservas.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Paciente {

    private Integer cedula;
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

}
