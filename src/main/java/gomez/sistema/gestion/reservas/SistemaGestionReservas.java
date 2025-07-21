package gomez.sistema.gestion.reservas;

import gomez.sistema.gestion.reservas.entities.Especialidad;

import gomez.sistema.gestion.reservas.controllers.MedicoController;
import gomez.sistema.gestion.reservas.entities.Medico;
import gomez.sistema.gestion.reservas.sql.Database;

import java.time.LocalTime;

public class SistemaGestionReservas {
    public static void main(String[] args) {



         medicoController.agregar(new Medico(Especialidad.Cardiologia, LocalTime.of(6, 00), LocalTime.of(15, 00), "Eduardo", 8976523));
         medicoController.agregar(new Medico(Especialidad.Dermatologia, LocalTime.of(9, 30), LocalTime.of(18, 15), "Alberto", 12346574));
         medicoController.agregar(new Medico(Especialidad.Pediatria, LocalTime.of(1, 00), LocalTime.of(12, 00), "Patricia", 476532234));

         //medicoController.listar().forEach(System.out::println);

        medicoController.buscarHorario(LocalTime.of(10, 00), LocalTime.of(12, 00)).forEach(System.out::println);


        medicoController.listar("Alberto", Especialidad.Cardiologia);

    }

    public final static MedicoController medicoController = new MedicoController();
    public final static Database database = new Database();
}
