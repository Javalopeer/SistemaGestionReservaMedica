package gomez.sistema.gestion.reservas.controllers;

import gomez.sistema.gestion.reservas.entities.Especialidad;
import gomez.sistema.gestion.reservas.entities.Medico;
import gomez.sistema.gestion.reservas.repository.MedicoRepositorio;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class MedicoController implements MedicoRepositorio {

    private final List<Medico> medicos;

    public MedicoController() {
        medicos = new ArrayList<Medico>();
    }

    @Override
    public List<Medico> listar(String nombre, Especialidad especialidad) {
        return medicos.stream()
                .filter(p->p.getNombre().equalsIgnoreCase(nombre) && p.getEspecialidad().equals(especialidad))
                .collect(Collectors.toList());
    }

    @Override
    public List<Medico> buscarHorario(LocalTime horarioInicio, LocalTime horarioFin) {
        return medicos.stream()
                .filter(p->
                        !p.getHorarioInicio().isAfter(horarioInicio) &&
                        !p.getHorarioFin().isBefore(horarioFin)
                )
                .collect(Collectors.toList());
    }


    @Override
    public List<Medico> listar() {
        return medicos;
    }

    @Override
    public Medico buscar(Integer id) {
        return medicos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void agregar(Medico medico) {
        if (!medicos.contains(medico)) {
            medicos.add(medico);
        } else {
            throw new NoSuchElementException("Ya existe un medico con esas caracteristicas.");
        }

    }

    @Override
    public void actualizar(Medico medico) {
        Medico act = buscar(medico.getId());
        if (act != null) {
            act.setNombre(medico.getNombre());
            if(!act.getEspecialidad().equals(medico.getEspecialidad())) {
                act.setEspecialidad(medico.getEspecialidad());
            }
            act.setTelefono(medico.getTelefono());
            act.setHorario(medico.getHorarioInicio(), medico.getHorarioFin());
        } else {
            throw new NoSuchElementException("No se encontro un medico con esas caracteristicas.");
        }

    }

    @Override
    public void eliminar(Integer id) {
        medicos.removeIf(p ->p.getId().equals(id));
    }


}
