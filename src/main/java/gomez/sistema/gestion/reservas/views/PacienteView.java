package gomez.sistema.gestion.reservas.views;

import gomez.sistema.gestion.reservas.dao.PacienteDao;
import gomez.sistema.gestion.reservas.entities.Paciente;
import gomez.sistema.gestion.reservas.error.AlertFactory;
import gomez.sistema.gestion.reservas.pdf.PdfExporterPac;
import gomez.sistema.gestion.reservas.sql.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PacienteView {

    private final PacienteDao pacDao = new PacienteDao(Database.getConnection());
    private boolean cargandoDatos = false;
    private Paciente pacienteOriginal;

    @FXML
    private Button btnExportarPDFPac;

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
    private TableView<Paciente> tablaPacientes;

    @FXML
    private TextField txtApellido;

    @FXML
    private TextField txtCedula;

    @FXML
    private TextField txtCedulaUpd;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtNombreCompleto;

    @FXML
    private TextField txtNombrePacienteUpd;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtTelefonoUpd;

    @FXML
    private Button updPaciente;


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

        setCamposEdicionEnabled(false);

        List<String> nombresPacientes = pacDao.buscar();
        TextFields.bindAutoCompletion(txtNombrePacienteUpd, nombresPacientes).getOnAutoCompleted();

        Map<String, Paciente> mapaMedicos = pacDao.obtenerTodos().stream()
                .collect(Collectors.toMap(m -> m.getNombre() + " " + m.getApellido(), m -> m));

        txtNombrePacienteUpd.setOnAction(event -> {
            String textoSeleccionado = txtNombrePacienteUpd.getText();
            Paciente pacienteSeleccionado  = mapaMedicos.get(textoSeleccionado);

            if (pacienteSeleccionado != null) {
                cargandoDatos = true;
                pacienteOriginal = pacienteSeleccionado;

                txtCedulaUpd.setText(pacienteSeleccionado.getCedula());
                txtNombreCompleto.setText(pacienteSeleccionado.getNombre() + " " + pacienteSeleccionado.getApellido());
                txtTelefonoUpd.setText(String.valueOf(pacienteSeleccionado.getTelefono()));
                cargandoDatos = false;
                setCamposEdicionEnabled(true);
                validarCambios();
            } else {
                setCamposEdicionEnabled(false);
                limpiarCamposEdicion();
            }
        });

        colCedula.setCellFactory(column -> crearCeldaCentrada());
        colNombre.setCellFactory(column -> crearCeldaCentrada());
        colApellido.setCellFactory(column -> crearCeldaCentrada());
        colTelefono.setCellFactory(column -> crearCeldaCentrada());


        txtCedulaUpd.textProperty().addListener((obs, oldVal, newVal) -> validarCambios());
        txtNombreCompleto.textProperty().addListener((obs, oldVal, newVal) -> validarCambios());
        txtTelefonoUpd.textProperty().addListener((obs, oldVal, newVal) -> validarCambios());

    }

    private void setCamposEdicionEnabled(boolean enabled) {
        txtCedulaUpd.setDisable(!enabled);
        txtNombreCompleto.setDisable(!enabled);
        txtTelefonoUpd.setDisable(!enabled);
    }

    private void limpiarCamposEdicion() {
        txtCedulaUpd.clear();
        txtNombreCompleto.clear();
        txtTelefonoUpd.clear();
    }

    private TableCell<Paciente, String> crearCeldaCentrada() {
        return new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                setAlignment(Pos.CENTER);
            }
        };
    }

    private boolean hayCambiosEnFormulario() {
        if (pacienteOriginal == null)
            return false;

        String[] partes = txtNombreCompleto.getText().trim().split(" ", 2);
        String nombre = partes[0];
        String apellido = partes.length > 1 ? partes[1] : "";

        return !txtCedulaUpd.getText().equals(pacienteOriginal.getCedula()) ||
                !nombre.equals(pacienteOriginal.getNombre()) ||
                !apellido.equals(pacienteOriginal.getApellido()) ||
                !txtTelefonoUpd.getText().equals(pacienteOriginal.getTelefono());
    }

    private void validarCambios(){
        if(cargandoDatos)
            return;
        updPaciente.setDisable(!hayCambiosEnFormulario());

    }

    @FXML
    private void agregarPaciente() {
        String  cedula = txtCedula.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();

        if(cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty()) {
            AlertFactory.mostrarError("Complete todos los campos.❌️");
            return;
        }

        if (!cedula.matches("\\d{9}")) {
            AlertFactory.mostrarError("La cédula debe contener exactamente 9 dígitos numéricos.");
            return;
        }

        if (pacDao.existeCedula(Integer.valueOf(cedula))) {
            AlertFactory.mostrarError("La cédula ya está registrada.❌️");
            return;
        }

        if (telefono.length() < 8 && telefono.length() >= 6) {
            AlertFactory.mostrarError("El telefono no puede ser mayor a 8 digitos ni menor a 6");
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

            AlertFactory.mostrarInfo("Exito", "Paciente eliminado correctamente ✅");

        }catch (Exception e) {
            AlertFactory.mostrarError("❌ Error: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarPaciente() {
        Paciente seleccionado = tablaPacientes.getSelectionModel().getSelectedItem();
        if(seleccionado != null) {
            pacDao.eliminar(seleccionado);
            tablaPacientes.getItems().remove(seleccionado);
            AlertFactory.mostrarInfo("Exito", "Paciente eliminado correctamente ✅");
        }else {
            AlertFactory.mostrarError("❗❗Seleccione un paciente para eliminar❗❗");
        }
    }

    @FXML
    private void actualizarPaciente() {
        try {
            String nombreCompleto = txtNombreCompleto.getText().trim();
            String[] partes = nombreCompleto.split(" ", 2);
            String nombre = partes[0];
            String apellido = partes.length > 1 ? partes[1] : "";

            String cedula = txtCedulaUpd.getText().trim();
            String telefono = txtTelefonoUpd.getText().trim();

            String cedulaOriginal = pacienteOriginal.getCedula();

            if (!cedula.equals(cedulaOriginal) && pacDao.existeCedula(Integer.valueOf(cedula))) {
                AlertFactory.mostrarError("La nueva cédula ya está registrada en otro paciente ❌");
                return;
            }

            pacienteOriginal.setCedula(cedula);
            pacienteOriginal.setNombre(nombre);
            pacienteOriginal.setApellido(apellido);
            pacienteOriginal.setTelefono(telefono);

            pacDao.actualizar(pacienteOriginal, cedulaOriginal);

            tablaPacientes.getItems().setAll(pacDao.obtenerTodos());

            txtCedulaUpd.clear();
            txtNombreCompleto.clear();
            txtTelefonoUpd.clear();

            AlertFactory.mostrarInfo("Éxito", "Paciente actualizado correctamente ✅");
            setCamposEdicionEnabled(false);

        } catch (Exception e) {
            e.printStackTrace();
            AlertFactory.mostrarError("Error al actualizar el paciente ❌");
        }
    }

    @FXML
    void exportarPaciente(ActionEvent actionEvent) {
        PdfExporterPac.exportarTablaPacientes(pacDao.obtenerTodos(), (Stage) btnExportarPDFPac.getScene().getWindow());
    }
}
