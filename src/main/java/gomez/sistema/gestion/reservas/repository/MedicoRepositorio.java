package gomez.sistema.gestion.reservas.repository;

import gomez.sistema.gestion.reservas.entities.Especialidad;
import gomez.sistema.gestion.reservas.entities.Medico;

import java.time.LocalTime;
import java.util.List;

public interface MedicoRepositorio extends RepositorioGeneral<Medico, Integer> {
    List<Medico> listar(String nombre, String apellido, Especialidad especialidad);
    List<Medico> buscarHorario(LocalTime horarioInicio, LocalTime horarioFin);
}
