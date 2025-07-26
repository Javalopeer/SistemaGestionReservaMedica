package gomez.sistema.gestion.reservas.views;

import gomez.sistema.gestion.reservas.dao.MedicoDao;
import gomez.sistema.gestion.reservas.entities.Especialidad;
import gomez.sistema.gestion.reservas.entities.Medico;
import gomez.sistema.gestion.reservas.error.AlertFactory;
import gomez.sistema.gestion.reservas.pdf.PdfExporterMed;
import gomez.sistema.gestion.reservas.sql.Database;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MedicoView {

    private final MedicoDao medDao = new MedicoDao(Database.getConnection());
    private Medico medicoOriginal;
    private boolean cargandoDatos = false;

    @FXML
    private TextField txtTelefonoUpd;

    @FXML
    private Button btnExportarPDFMed;

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

        colNombreCompleto.setCellValueFactory(cellData -> {
            String nombre = cellData.getValue().getNombre();
            String apellido = cellData.getValue().getApellido();
            String nombreCompleto = (nombre != null ? nombre : "") +
                    (apellido != null && !apellido.trim().isEmpty() ? " " + apellido : "");
            return new SimpleStringProperty(nombreCompleto.trim());
        });
        colEspecialidad.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEspecialidad().name()));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colHorario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHorarioInicio() + " - " + cellData.getValue().getHorarioFin()));

        txtNombreUpd.textProperty().addListener((obs, oldVal, newVal) -> validarCambios());
        txtTelefonoUpd.textProperty().addListener((obs, oldVal, newVal) -> validarCambios());
        txtHorarioEntrUpd.textProperty().addListener((obs, oldVal, newVal) -> validarCambios());
        txtHorarioSalUpd.textProperty().addListener((obs, oldVal, newVal) -> validarCambios());
        comboEspUpd.valueProperty().addListener((obs, oldVal, newVal) -> validarCambios());

        setCamposEdicionEnabled(false);

        colNombreCompleto.setCellFactory(column -> crearCeldaCentrada());
        colEspecialidad.setCellFactory(column -> crearCeldaCentrada());
        colHorario.setCellFactory(column -> crearCeldaCentrada());
        colTelefono.setCellFactory(column -> crearCeldaCentrada());

        tablaMedicos.getItems().addAll(medDao.obtenerTodos());
        tablaMedicos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            delMedico.setDisable(newValue == null);
        });
        delMedico.setDisable(true);


        comboEsp.getItems().addAll(Especialidad.values());

        comboEspUpd.getItems().addAll(Especialidad.values());

        List<String> nombresMedicos = medDao.buscar();
        TextFields.bindAutoCompletion(txtNombreMedicoUpd, nombresMedicos).getOnAutoCompleted();

        Map<String, Medico> mapaMedicos = medDao.obtenerTodos().stream()
                .collect(Collectors.toMap(m -> m.getNombre() + " " + m.getApellido(), m -> m));

        txtNombreMedicoUpd.setOnAction(event -> {
            String textoSeleccionado = txtNombreMedicoUpd.getText();
            Medico medicoSeleccionado = mapaMedicos.get(textoSeleccionado);

            if (medicoSeleccionado != null) {
                cargandoDatos = true;
                medicoOriginal = medicoSeleccionado;

                txtNombreUpd.setText(medicoSeleccionado.getNombre() + " " + medicoSeleccionado.getApellido());
                comboEspUpd.setValue(medicoSeleccionado.getEspecialidad());
                txtTelefonoUpd.setText(String.valueOf(medicoSeleccionado.getTelefono()));
                txtHorarioEntrUpd.setText(String.valueOf(medicoSeleccionado.getHorarioInicio()));
                txtHorarioSalUpd.setText(String.valueOf(medicoSeleccionado.getHorarioFin()));

                cargandoDatos = false;
                setCamposEdicionEnabled(true);
                validarCambios();
            } else {
                setCamposEdicionEnabled(false);
                limpiarCamposEdicion();
            }
        });
    }

    private TableCell<Medico, String> crearCeldaCentrada() {
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
        if (medicoOriginal == null)
            return false;

        String[] partes = txtNombreUpd.getText().trim().split(" ", 2);
        String nombre = partes[0];
        String apellido = partes.length > 1 ? partes[1] : "";

        return !nombre.equals(medicoOriginal.getNombre()) ||
                !apellido.equals(medicoOriginal.getApellido()) ||
                !String.valueOf(comboEspUpd.getValue()).equals(String.valueOf(medicoOriginal.getEspecialidad())) ||
                !txtTelefonoUpd.getText().equals(medicoOriginal.getTelefono()) ||
                !txtHorarioEntrUpd.getText().equals(medicoOriginal.getHorarioInicio().toString()) ||
                !txtHorarioSalUpd.getText().equals(medicoOriginal.getHorarioFin().toString());
    }

    private void validarCambios() {
        if (cargandoDatos) return;
        updMedico.setDisable(!hayCambiosEnFormulario());
    }

    private void limpiarCamposEdicion() {
        txtNombreUpd.clear();
        comboEspUpd.setValue(null);
        txtTelefonoUpd.clear();
        txtHorarioEntrUpd.clear();
        txtHorarioSalUpd.clear();
    }

    private void setCamposEdicionEnabled(boolean enabled) {
        txtNombreUpd.setDisable(!enabled);
        comboEspUpd.setDisable(!enabled);
        txtTelefonoUpd.setDisable(!enabled);
        txtHorarioEntrUpd.setDisable(!enabled);
        txtHorarioSalUpd.setDisable(!enabled);
        updMedico.setDisable(!enabled);
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

        LocalTime horarioInicio = LocalTime.parse(horarioInicioStr, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime horarioFin = LocalTime.parse(horarioFinStr, DateTimeFormatter.ofPattern("HH:mm"));

        // Validaciones
        if (nombre.isEmpty() || especialidad == null || horarioInicioStr.isEmpty() ||
                horarioFinStr.isEmpty() || telefonoStr.isEmpty()) {
            AlertFactory.mostrarError("Complete todos los campos.");
            return;
        }

        if (telefonoStr.length() > 8 || telefonoStr.length() < 4){
            AlertFactory.mostrarError("El número de teléfono no puede tener más de 8 dígitos y menos de 4.");
            return;
        }

        if(horarioInicio.isAfter(horarioFin) && horarioInicioStr.equals(horarioFinStr)){
            AlertFactory.mostrarError("El horario inicial no puede ser mayor al final.");
            return;
        }

        try {

            String telefono = telefonoStr.trim();

            Medico medico = new Medico();
            medico.setNombre(nombre);
            medico.setApellido(apellido);
            medico.setEspecialidad(especialidad);
            medico.setHorarioInicio(horarioInicio);
            medico.setHorarioFin(horarioFin);
            medico.setTelefono(telefono);

            medDao.insertar(medico);
            tablaMedicos.getItems().add(medico);

            txtNombreCompleto.clear();
            comboEsp.setValue(null);
            txtHorarioEnt.clear();
            txtHorarioSali.clear();
            txtTelefono.clear();

            AlertFactory.mostrarInfo("Éxito", "Médico agregado correctamente ✅");

        } catch (Exception e) {
            e.printStackTrace();
            AlertFactory.mostrarError("Error al agregar el médico.");
        }
    }

    @FXML
    private void eliminarMedico() {
        Medico seleccionado = tablaMedicos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
                medDao.eliminar(seleccionado);
                tablaMedicos.getItems().remove(seleccionado);
                AlertFactory.mostrarInfo("Éxito","Médico eliminado correctamente ✅");
        } else {
            AlertFactory.mostrarError("❗❗Seleccione un médico para eliminar❗❗");
        }
    }

    @FXML
    private void actualizarMedico() {
        try{
            String nombreCompleto = txtNombreUpd.getText().trim();
            String[] partes = nombreCompleto.split(" ", 2);
            String nombre = partes[0];
            String apellido = partes.length > 1 ? partes[1] : "";

            Especialidad especialidad = comboEspUpd.getValue();
            String telefono = txtTelefonoUpd.getText();
            String horarioEntrada = txtHorarioEntrUpd.getText();
            String horarioSalida = txtHorarioSalUpd.getText();

            String textoSeleccionado = txtNombreMedicoUpd.getText();
            Optional<Medico> medicoOpt = medDao.obtenerTodos().stream()
                    .filter(m -> (m.getNombre() + " " + m.getApellido()).equalsIgnoreCase(textoSeleccionado))
                    .findFirst();

            if (medicoOpt.isEmpty()) {
                AlertFactory.mostrarError("Médico no encontrado ❌");
                return;
            }

            Medico medico = medicoOpt.get();
            medico.setNombre(nombre);
            medico.setApellido(apellido);
            medico.setEspecialidad(especialidad);
            medico.setTelefono(telefono);
            medico.setHorarioInicio(LocalTime.parse(horarioEntrada));
            medico.setHorarioFin(LocalTime.parse(horarioSalida));

            medDao.actualizar(medico);
            tablaMedicos.getItems().setAll(medDao.obtenerTodos());

            txtNombreUpd.clear();
            comboEspUpd.setValue(null);
            txtTelefonoUpd.clear();
            txtHorarioEntrUpd.clear();
            txtHorarioSalUpd.clear();
            txtNombreMedicoUpd.clear();

            AlertFactory.mostrarInfo("Éxito","Médico actualizado correctamente ✅");
            setCamposEdicionEnabled(false);

        }catch (Exception e){
            e.printStackTrace();
            AlertFactory.mostrarError("Error al actualizar el medico ❌");
        }
    }

    @FXML
    void exportarMedico(ActionEvent event) {
        PdfExporterMed.exportarTablaMedicos(medDao.obtenerTodos(), (Stage) btnExportarPDFMed.getScene().getWindow());
    }
}


