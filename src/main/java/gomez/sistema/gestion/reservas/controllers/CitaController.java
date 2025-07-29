package gomez.sistema.gestion.reservas.controllers;

import gomez.sistema.gestion.reservas.entities.Cita;
import gomez.sistema.gestion.reservas.entities.Medico;
import gomez.sistema.gestion.reservas.entities.Paciente;
import gomez.sistema.gestion.reservas.error.AlertFactory;
import gomez.sistema.gestion.reservas.repository.CitaRepositorio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CitaController implements CitaRepositorio {

    private final List<Cita> citas;

    public CitaController() {
        citas = new ArrayList<Cita>();
    }

    @Override
    public Cita asignarCita(Paciente paciente, Medico medico) {
        LocalDate fecha = LocalDate.now();
        LocalTime hora = LocalTime.now();

        if(existeConflicto(fecha, medico, hora)) {
            AlertFactory.mostrarError("Hay conflicto de cita. No se puede asignar la cita. ✖️");
        }

        Cita nuevaCita = new Cita(fecha, hora, medico, paciente);
        citas.add(nuevaCita);
        return nuevaCita;
    }

    @Override
    public List<Cita> obtenerFechaCita(Date fecha) {
        return List.of();
    }

    public List<Cita> obtenerFechaCita(LocalDate fecha) {
        return citas.stream()
                .filter(c->c.getFecha().equals(fecha))
                .collect(Collectors.toList());
    }

    @Override
    public List<Cita> obtenerMedicoCita(Medico medico) {
        return citas.stream()
                .filter(c->c.getMedico().equals(medico))
                .collect(Collectors.toList());
    }

    @Override
    public List<Cita> obtenerPacienteCita(Paciente paciente) {
        return citas.stream()
                .filter((cita) -> cita.getPaciente().equals(paciente))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existeConflicto(Date fecha, Medico medico, LocalTime hora) {
        return false;
    }

    public boolean existeConflicto(LocalDate fecha, Medico medico, LocalTime hora) {
        return citas.stream()
                .anyMatch((cita) -> cita.getFecha().equals(fecha)
                            && cita.getMedico().equals(medico)
                            && cita.getHora().equals(hora));
    }

    @Override
    public List<Cita> listar() {
        return citas.stream().toList();
    }

    @Override
    public Cita buscar(Integer id) {
        return citas.stream()
                .filter(c->c.getIdCita().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void agregar(Cita cita) {
        citas.add(cita);
    }

    @Override
    public void actualizar(Cita cita) {
        for (int i = 0; i < citas.size(); i++) {
            if (citas.get(i).getIdCita().equals(cita.getIdCita())) {
                citas.set(i, cita);
                break;
            } else {
                AlertFactory.mostrarError("⚠️ No se encontró la cita con ID: " + cita.getIdCita());
            }
        }
    }

    @Override
    public void eliminar(Integer id) {
        citas.removeIf(c->c.getIdCita().equals(id));

    }
}
