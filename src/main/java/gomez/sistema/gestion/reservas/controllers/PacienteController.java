package gomez.sistema.gestion.reservas.controllers;

import gomez.sistema.gestion.reservas.entities.Paciente;
import gomez.sistema.gestion.reservas.repository.PacienteRepositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class PacienteController implements PacienteRepositorio {

    private List<Paciente> pacientes;

    public PacienteController() {
        pacientes = new ArrayList<Paciente>();
    }

    @Override
    public List<Paciente> listar() {
        return pacientes;
    }

    @Override
    public Paciente buscar(Float t) {
        return null;
    }


    public Paciente buscarName(String cedula) {
        return pacientes.stream()
                .filter(p-> p.getCedula().equals(cedula))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void agregar(Paciente paciente) {
        pacientes.add(paciente);
    }

    @Override
    public void actualizar(Paciente paciente) {
        Paciente p = buscarName(paciente.getCedula());
        if (p != null) {
            p.setNombre(paciente.getNombre());
            p.setApellido(paciente.getApellido());
        } else {
            throw new NoSuchElementException("No se encontro al paciente de id " + paciente.getCedula());
        }
    }

    @Override
    public void eliminar(Float cedula) {
        pacientes.removeIf(p -> p.getCedula().equals(cedula));
    }

}




