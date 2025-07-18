package gomez.sistema.gestion.reservas.entities;

public class HistorialMedico {

    private Integer idHistorialMedico;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;
    private Cita cita;
    private Medico medico;
    private static int ultimoId;

    public HistorialMedico() {
        this.idHistorialMedico =++ ultimoId;
    }

    public HistorialMedico(Cita cita, String diagnostico, Integer idHistorialMedico, Medico medico, String observaciones, String tratamiento) {
        this();
        this.cita = cita;
        this.diagnostico = diagnostico;
        this.idHistorialMedico = idHistorialMedico;
        this.medico = medico;
        this.observaciones = observaciones;
        this.tratamiento = tratamiento;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Integer getIdHistorialMedico() {
        return idHistorialMedico;
    }

    public void setIdHistorialMedico(Integer idHistorialMedico) {
        this.idHistorialMedico = idHistorialMedico;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }
}
