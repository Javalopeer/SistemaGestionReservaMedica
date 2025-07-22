package gomez.sistema.gestion.reservas.views;

import gomez.sistema.gestion.reservas.controllers.PacienteController;
import gomez.sistema.gestion.reservas.dao.PacienteDao;
import gomez.sistema.gestion.reservas.entities.Paciente;
import gomez.sistema.gestion.reservas.sql.Database;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class PacienteView {

    private final PacienteController controller = new PacienteController();
    private final PacienteDao pacDao = new PacienteDao(Database.getConnection());

    @FXML
    private Button aggPaciente;

    @FXML
    private TableColumn<Paciente, String> colApellido;

    @FXML
    private TableColumn<Paciente, String> colCedula;

    @FXML
    private TableColumn<Paciente, String> colNombre;

    @FXML
    private TableColumn<Paciente, String> colTelefono;

    @FXML
    private Button delPaciente;

    @FXML
    private Label lblAccion;

    @FXML
    private Label lblAccionDel;

    @FXML
    private TableView<Paciente> tablaPacientes;

    @FXML
    private TextField txtApellido;

    @FXML
    private TextField txtCedula;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTelefono;


    @FXML
    public void initialize() {

        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        tablaPacientes.getItems().addAll(pacDao.obtenerTodos());
        tablaPacientes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            delPaciente.setDisable(newValue == null);
        });
        delPaciente.setDisable(true);
    }

    @FXML
    private void agregarPaciente() {
        Integer cedula = Integer.valueOf(txtCedula.getText().trim());
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();
        if(nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty()) {
            lblAccion.setText("Complete todos los campos.❌️");
            return;
        }
        if (pacDao.existeCedula(cedula)) {
            lblAccion.setText("La cédula ya está registrada.❌️");
            return;
        }
        try {
            Paciente paciente = new Paciente();
            paciente.setCedula(cedula);
            paciente.setNombre(nombre);
            paciente.setApellido(apellido);
            paciente.setTelefono(telefono);

            pacDao.insertar(paciente);
            tablaPacientes.getItems().add(paciente);

            txtCedula.clear();
            txtNombre.clear();
            txtApellido.clear();
            txtTelefono.clear();

            lblAccion.setText("Paciente agregado correctamente.✅");

        }catch (Exception e) {
            lblAccion.setText("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarPaciente() {
        Paciente seleccionado = tablaPacientes.getSelectionModel().getSelectedItem();
        if(seleccionado != null) {
            pacDao.eliminar(seleccionado);
            tablaPacientes.getItems().remove(seleccionado);
            lblAccionDel.setText("Paciente eliminado correctamente.✅");
        }else {
            lblAccionDel.setText("❗❗Seleccione un paciente para eliminar❗❗");
        }
    }
}
