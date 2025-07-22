package gomez.sistema.gestion.reservas.views;

import gomez.sistema.gestion.reservas.controllers.MedicoController;
import gomez.sistema.gestion.reservas.dao.MedicoDao;
import gomez.sistema.gestion.reservas.entities.Especialidad;
import gomez.sistema.gestion.reservas.entities.Medico;
import gomez.sistema.gestion.reservas.sql.Database;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MedicoView {

    private final MedicoController controller = new MedicoController();
    private final MedicoDao medDao = new MedicoDao(Database.getConnection());

    @FXML
    private BorderPane borderPane;

    @FXML
    private ToggleButton btnInicio;

    @FXML
    private Label lblAccion;

    @FXML
    private Label lblAccionDel;

    @FXML
    private Button aggMedico;

    @FXML
    private TableColumn<Medico, String> colEspecialidad;

    @FXML
    private TableColumn<Medico, String> colHorario;

    @FXML
    private TableColumn<Medico, String> colNombre;

    @FXML
    private TableColumn<Medico, String> colTelefono;

    @FXML
    private ComboBox<Especialidad> comboEsp;

    @FXML
    private TableView<Medico> tablaMedicos;

    @FXML
    private TextField txtHorarioEnt;

    @FXML
    private TextField txtHorarioSali;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTelefono;

    @FXML
    private Button delMedico;

    @FXML
    void initialize() {

        try (Connection con = Database.getConnection()) {
            if (con != null) {
                System.out.println("Conectado exitosamente a la base de datos de Reservas Medicas. ✅");
            }
        } catch (SQLException e) {
            System.out.println("✖️ Error al conectar: " + e.getMessage());
        }

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEspecialidad.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEspecialidad().name()));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colHorario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHorarioInicio() + " - " + cellData.getValue().getHorarioFin()));

        tablaMedicos.getItems().addAll(medDao.obtenerTodos());
        tablaMedicos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            delMedico.setDisable(newValue == null);
        });
        delMedico.setDisable(true);

        comboEsp.getItems().addAll(Especialidad.values());


    }

    @FXML
    private void agregarMedico() {
        String nombre = txtNombre.getText().trim();
        Especialidad especialidad = comboEsp.getValue();
        String horarioInicioStr = txtHorarioEnt.getText().trim();
        String horarioFinStr = txtHorarioSali.getText().trim();
        String telefonoStr = txtTelefono.getText().trim();

        // Validaciones
        if (nombre.isEmpty() || especialidad == null || horarioInicioStr.isEmpty() ||
                horarioFinStr.isEmpty() || telefonoStr.isEmpty()) {
            lblAccion.setText("Complete todos los campos.❌️");
            return;
        }

        try {
            LocalTime horarioInicio = LocalTime.parse(horarioInicioStr, DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime horarioFin = LocalTime.parse(horarioFinStr, DateTimeFormatter.ofPattern("HH:mm"));
            int telefono = Integer.parseInt(telefonoStr);

            Medico medico = new Medico();
            medico.setNombre(nombre);
            medico.setEspecialidad(especialidad);
            medico.setHorarioInicio(horarioInicio);
            medico.setHorarioFin(horarioFin);
            medico.setTelefono(telefono);

            medDao.insertar(medico);
            tablaMedicos.getItems().add(medico);

            // Limpiar campos
            txtNombre.clear();
            comboEsp.setValue(null);
            txtHorarioEnt.clear();
            txtHorarioSali.clear();
            txtTelefono.clear();

            lblAccion.setText("Médico agregado correctamente.✅");

        } catch (Exception e) {
            lblAccion.setText("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarMedico() {
        Medico seleccionado = tablaMedicos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
                medDao.eliminar(seleccionado);
                tablaMedicos.getItems().remove(seleccionado);
                lblAccionDel.setText("Médico eliminado correctamente.✅");
        } else {
            lblAccionDel.setText("❗❗Seleccione un médico para eliminar❗❗");
        }
    }
}
