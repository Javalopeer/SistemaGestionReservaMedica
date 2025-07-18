package gomez.sistema.gestion.reservas.entities;

import lombok.Data;

@Data
public class PacienteMedico {

    private Medico medico;
    private Paciente paciente;

    public PacienteMedico() {
    }

    public PacienteMedico(Medico medico, Paciente paciente) {
        this.medico = medico;
        this.paciente = paciente;
    }

}
