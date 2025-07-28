package gomez.sistema.gestion.reservas.views;

import gomez.sistema.gestion.reservas.dao.CitasDao;
import gomez.sistema.gestion.reservas.dao.Database;
import gomez.sistema.gestion.reservas.dao.MedicoDao;
import gomez.sistema.gestion.reservas.dao.PacienteDao;
import gomez.sistema.gestion.reservas.entities.Medico;
import gomez.sistema.gestion.reservas.entities.Paciente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

public class CitasView {

    private final MedicoDao medDao = new MedicoDao(Database.getConnection());
    private final PacienteDao pacDao = new PacienteDao(Database.getConnection());
    private final CitasDao citasDao = new CitasDao(Database.getConnection(), pacDao, medDao);


    @FXML
    private ComboBox<Medico> boxMedico;

    @FXML
    private ComboBox<Paciente> boxPaciente;

    @FXML
    private Button idBotonCita;

    @FXML
    private DatePicker pickerDiaCita;

    @FXML
    private ComboBox<Medico> verificarHorarioXDoctor;

    @FXML
    void initialize() {

        boxMedico.setConverter(new StringConverter<Medico>() {
            @Override
            public String toString(Medico medico) {
                return medico != null ? medico.getNombre() + " " + medico.getApellido(): "";
            }

            @Override
            public Medico fromString(String string) {
                return null;
            }
        });

        boxMedico.getItems().addAll(medDao.obtenerNombreApellido());

    }


    @FXML
    void asignarCita(ActionEvent event) {

    }

}
