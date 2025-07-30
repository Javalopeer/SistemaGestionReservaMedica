package gomez.sistema.gestion.reservas.test;

import gomez.sistema.gestion.reservas.controllers.PacienteController;
import gomez.sistema.gestion.reservas.entities.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PacienteControllerTest {

    private PacienteController controller;

    @BeforeEach
    void setUp() {
        controller = new PacienteController();
    }

    @Test
    void testAgregarYBuscarPaciente() {
        Paciente p = new Paciente("12345678", "Ana", "López", "8888-9999");
        controller.agregar(p);

        Paciente encontrado = controller.buscarName(("12345678"));
        assertNotNull(encontrado);
        assertEquals("Ana", encontrado.getNombre());
        assertEquals("López", encontrado.getApellido());
    }

    @Test
    void testActualizarPaciente() {
        Paciente p = new Paciente("1231234", "Pedro", "Gómez", "8888-8888");
        controller.agregar(p);

        Paciente actualizado = new Paciente("1231234", "Pedro", "Ramírez", "8888-8888");
        controller.actualizar(actualizado);

        Paciente result = controller.buscarName(("1231234"));
        assertEquals("Pedro", result.getNombre());
        assertEquals("Ramírez", result.getApellido());
        assertEquals("8888-8888", result.getTelefono());
    }

    @Test
    void testEliminarPaciente() {
        Paciente p = new Paciente("55594253", "Luis", "Zamora", "6000-0000");
        controller.agregar(p);

        controller.eliminar(Float.valueOf("55594253"));
        Paciente eliminado = controller.buscar(Float.valueOf("55594253"));
        assertNull(eliminado);
    }

    @Test
    void testListarPacientes() {
        controller.agregar(new Paciente("11234414", "A", "B", "1111"));
        controller.agregar(new Paciente("212355675", "C", "D", "2222"));

        List<Paciente> lista = controller.listar();
        assertEquals(2, lista.size());
    }
}
