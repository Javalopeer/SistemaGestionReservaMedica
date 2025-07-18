package gomez.sistema.gestion.reservas.repository;

import gomez.sistema.gestion.reservas.entities.Cita;
import gomez.sistema.gestion.reservas.entities.Medico;
import gomez.sistema.gestion.reservas.entities.Paciente;

public interface CitaRepositorio extends RepositorioGeneral<Cita, Integer> {
    Cita asignarCita(Paciente paciente, Medico medico);

}
