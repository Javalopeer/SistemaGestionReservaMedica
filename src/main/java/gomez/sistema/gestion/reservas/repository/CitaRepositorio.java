package gomez.sistema.gestion.reservas.repository;

import gomez.sistema.gestion.reservas.entities.Cita;
import gomez.sistema.gestion.reservas.entities.Medico;
import gomez.sistema.gestion.reservas.entities.Paciente;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public interface CitaRepositorio extends RepositorioGeneral<Cita, Integer> {
    Cita asignarCita(Paciente paciente, Medico medico);
    List<Cita> obtenerFechaCita(Date fecha);
    List<Cita> obtenerMedicoCita(Medico medico);
    List<Cita> obtenerPacienteCita(Paciente paciente);
    boolean existeConflicto(Date fecha, Medico medico, LocalTime hora);

}
