package gomez.sistema.gestion.reservas.entities;

public class PacienteMedico {

    private Medico medico;
    private Paciente paciente;

    public PacienteMedico() {
    }

    public PacienteMedico(Medico medico, Paciente paciente) {
        this.medico = medico;
        this.paciente = paciente;
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
