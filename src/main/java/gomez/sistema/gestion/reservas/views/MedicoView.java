package gomez.sistema.gestion.reservas.views;

import gomez.sistema.gestion.reservas.controllers.MedicoController;
import gomez.sistema.gestion.reservas.dao.MedicoDao;
import gomez.sistema.gestion.reservas.entities.Especialidad;
import gomez.sistema.gestion.reservas.entities.Medico;
import gomez.sistema.gestion.reservas.sql.Database;
import javafx.beans.property.SimpleStringProperty;
import org.controlsfx.control.textfield.TextFields;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MedicoView {

    private final MedicoController controller = new MedicoController();
    private final MedicoDao medDao = new MedicoDao(Database.getConnection());

    @FXML
    private TextField txtTelefonoUpd;

    @FXML
    private Button updMedico;

    @FXML
    private TextField txtNombreUpd;

    @FXML
    private TextField txtHorarioEntrUpd;

    @FXML
    private TextField txtHorarioSalUpd;

    @FXML
    private ComboBox<Especialidad> comboEspUpd;

    @FXML
    private TextField txtNombreMedicoUpd;

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
    private TableColumn<Medico, String> colNombreCompleto;

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
    private TextField txtNombreCompleto;

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

        colNombreCompleto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre() + " " + cellData.getValue().getApellido()));
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

        comboEspUpd.getItems().addAll(Especialidad.values());

        List<String> nombresMedicos = medDao.buscar();
        TextFields.bindAutoCompletion(txtNombreMedicoUpd, nombresMedicos).getOnAutoCompleted();



    }

    @FXML
    private void agregarMedico() {
        String nombreCompleto = txtNombreCompleto.getText().trim();
        String[] partes = nombreCompleto.split(" ", 2);
        String nombre = partes[0];
        String apellido = partes.length > 1 ? partes[1] : "";

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
            medico.setApellido(apellido);
            medico.setEspecialidad(especialidad);
            medico.setHorarioInicio(horarioInicio);
            medico.setHorarioFin(horarioFin);
            medico.setTelefono(telefono);

            medDao.insertar(medico);
            tablaMedicos.getItems().add(medico);

            // Limpiar campos
            txtNombreCompleto.clear();
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

    @FXML
    private void actualizarMedico() {

    }
}
