package gomez.sistema.gestion.reservas.test;

import gomez.sistema.gestion.reservas.controllers.MedicoController;
import gomez.sistema.gestion.reservas.entities.Especialidad;
import gomez.sistema.gestion.reservas.entities.Medico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MedicoControllerTest {

    private MedicoController controller;

    @BeforeEach
    void setUp() {
        controller = new MedicoController();
    }

    @Test
    void testAgregarYBuscarMedico() {
        Medico m = new Medico(1, "Juan", "Perez", Especialidad.Neumología, "12345678", LocalTime.of(8, 0), LocalTime.of(14, 0));
        controller.agregar(m);

        Medico encontrado = controller.buscar(1);
        assertNotNull(encontrado);
        assertEquals("Juan", encontrado.getNombre());
    }

    @Test
    void testActualizarMedico() {
        Medico m = new Medico(2, "Laura", "Gomez", Especialidad.Oncología, "11111111", LocalTime.of(9, 0), LocalTime.of(15, 0));
        controller.agregar(m);

        Medico actualizado = new Medico(2, "Laura", "Gomez", Especialidad.Dermatología, "99999999", LocalTime.of(10, 0), LocalTime.of(16, 0));
        controller.actualizar(actualizado);

        Medico result = controller.buscar(2);
        assertEquals(Especialidad.Oncología, result.getEspecialidad());
        assertEquals("99999999", result.getTelefono());
    }

    @Test
    void testEliminarMedico() {
        Medico m = new Medico(3, "Pedro", "Diaz", Especialidad.Neurología, "22222222", LocalTime.of(7, 0), LocalTime.of(13, 0));
        controller.agregar(m);

        controller.eliminar(3);
        assertNull(controller.buscar(3));
    }

    @Test
    void testListarPorHorario() {
        controller.agregar(new Medico(4, "Ana", "Soto", Especialidad.Ortopedia, "33333333", LocalTime.of(8, 0), LocalTime.of(12, 0)));
        controller.agregar(new Medico(5, "Luis", "Marin", Especialidad.Ortopedia, "44444444", LocalTime.of(14, 0), LocalTime.of(18, 0)));

        List<Medico> encontrados = controller.buscarHorario(LocalTime.of(8, 0), LocalTime.of(12, 0));
        assertEquals(1, encontrados.size());
        assertEquals("Ana", encontrados.get(0).getNombre());
    }

    @Test
    void testListarPorNombreApellidoEspecialidad() {
        controller.agregar(new Medico(6, "Carlos", "Jimenez", Especialidad.Dermatología, "55555555", LocalTime.of(10, 0), LocalTime.of(16, 0)));
        List<Medico> resultado = controller.listar("Carlos", "Jimenez", Especialidad.Dermatología);

        assertEquals(1, resultado.size());
        assertEquals("Carlos", resultado.get(0).getNombre());
    }
}
