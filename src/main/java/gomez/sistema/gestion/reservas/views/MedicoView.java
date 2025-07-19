package gomez.sistema.gestion.reservas.views;

import gomez.sistema.gestion.reservas.controllers.MedicoController;
import gomez.sistema.gestion.reservas.entities.Especialidad;
import gomez.sistema.gestion.reservas.entities.Medico;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.time.LocalTime;

public class MedicoView {

    private final MedicoController controller = new MedicoController();

    @FXML
    private ComboBox<Especialidad> comboEspecialidad;

    @FXML
    private Label lblAccion;

    @FXML
    private Label lblEntidad;

    @FXML
    private Label lblEspecialidad;

    @FXML
    private Label lblHorario;

    @FXML
    private Label lblHorario1;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblTelefono;

    @FXML
    private TextField txtHorarioEntrada;

    @FXML
    private TextField txtHorarioSalida;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTelefono;

    @FXML
    void agregarMedico(ActionEvent event) {
//        Medico medico = new Medico();
//        medico.setNombre(txtNombre.getText());
//        medico.setEspecialidad((Especialidad) comboEspecialidad.getValue());
//        medico.setHorarioInicio(LocalTime.parse(txtHorarioEntrada.getText()));
//        medico.setHorarioFin(LocalTime.parse(txtHorarioSalida.getText()));
//        medico.setTelefono(Integer.parseInt(txtTelefono.getText()));
//        controller.agregar(medico);
    }

     @FXML
    void initialize(){
//         comboEspecialidad.getItems().addAll(Especialidad.values());
//
//         comboEspecialidad.setConverter(new StringConverter<Especialidad>() {
//             @Override
//             public String toString(Especialidad especialidad) {
//                 return especialidad != null ? especialidad.name() : "";
//             }
//
//             @Override
//             public Especialidad fromString(String string) {
//                 return Especialidad.valueOf(string);
//             }
//         });
     }


}
