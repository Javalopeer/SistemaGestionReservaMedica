package gomez.sistema.gestion.reservas.views;

import gomez.sistema.gestion.reservas.dao.*;
import gomez.sistema.gestion.reservas.entities.*;
import gomez.sistema.gestion.reservas.error.AlertFactory;
import gomez.sistema.gestion.reservas.pdf.PdfExporterCita;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CitasView {

    private final MedicoDao medDao = new MedicoDao(Database.getConnection());
    private final PacienteDao pacDao = new PacienteDao(Database.getConnection());
    private final CitasDao citasDao = new CitasDao(Database.getConnection(), pacDao, medDao);
    private final FacturaDao facturaDao = new FacturaDao(Database.getConnection(), citasDao);
    private ObservableList<Cita> citas = FXCollections.observableArrayList();
    private Cita citaOriginal;

    @FXML
    private ComboBox<Especialidad> boxEspecialidad;

    @FXML
    private ComboBox<Medico> boxMedico;

    @FXML
    private ComboBox<Paciente> boxPaciente;

    @FXML
    private Button idBotonCita;

    @FXML
    private DatePicker pickerDiaCita;

    @FXML
    private Label labelHorario;

    @FXML
    private ComboBox<LocalTime> boxHoraCita;

    @FXML
    private TableView<Cita> tablaCitas;

    @FXML
    private TableColumn<Cita, String> colFecha;

    @FXML
    private TableColumn<Cita, String> colHora;

    @FXML
    private TableColumn<Cita, String> colMedico;

    @FXML
    private TableColumn<Cita, String> colPaciente;

    @FXML
    private ComboBox<Especialidad> boxEspecialidadActu;

    @FXML
    private ComboBox<LocalTime> boxHoraActu;

    @FXML
    private ComboBox<Medico> boxMedicoActu;

    @FXML
    private TableColumn<Cita, String> colId;

    @FXML
    private Button idBotonActualizarCita;

    @FXML
    private Label labelHorarioActu;

    @FXML
    private DatePicker pickerDiaActu;

    @FXML
    private TextField txtIdCita;

    @FXML
    private TextField txtPaciente;

    @FXML
    private BarChart<String, Number> graficoCitas;

    @FXML
    private CategoryAxis ejeX;

    @FXML
    private NumberAxis ejeY;


    @FXML
    private Button btnExportarPDF;

    @FXML
    void initialize() {

        // 1. Inhabilitar componentes según flujo
        boxMedico.setDisable(true);
        boxHoraCita.setDisable(true);
        pickerDiaCita.setDisable(true);
        idBotonCita.setDisable(true);

        // 2. Cargar pacientes
        boxPaciente.setConverter(new StringConverter<>() {
            public String toString(Paciente paciente) {
                return paciente != null ? paciente.getNombre() + " " + paciente.getApellido() : "";
            }

            public Paciente fromString(String s) { return null; }
        });
        boxPaciente.getItems().addAll(pacDao.obtenerTodos());

        // 3. Cargar especialidades
        boxEspecialidad.setConverter(new StringConverter<>() {
            public String toString(Especialidad especialidad) {
                return especialidad != null ? especialidad.name() : "";
            }

            public Especialidad fromString(String s) {
                return Especialidad.valueOf(s);
            }
        });
        boxEspecialidad.getItems().addAll(Especialidad.values());

        // 4. Configurar combo de médicos
        boxMedico.setConverter(new StringConverter<>() {
            public String toString(Medico medico) {
                return medico != null ? medico.getNombre() + " " + medico.getApellido() : "";
            }

            public Medico fromString(String s) { return null; }
        });

        boxMedicoActu.setConverter(new StringConverter<>() {
            public String toString(Medico medico) {
                return medico != null ? medico.getNombre() + " " + medico.getApellido() : "";
            }

            public Medico fromString(String s) { return null; }
        });

        // 5. Listener: Especialidad → médicos
        boxEspecialidad.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boxMedico.getItems().clear();
            boxMedico.setDisable(true);
            boxHoraCita.getItems().clear();
            boxHoraCita.setDisable(true);
            pickerDiaCita.setDisable(true);
            idBotonCita.setDisable(true);
            labelHorario.setText(" - ");

            if (newVal != null) {
                List<Medico> medicos = medDao.obtenerPorEspecialidad(newVal);
                boxMedico.getItems().addAll(medicos);
                boxMedico.setDisable(false);
            }
        });

        // 6. Listener: Médico → horarios
        boxMedico.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boxHoraCita.getItems().clear();
            pickerDiaCita.setDisable(true);
            idBotonCita.setDisable(true);

            if (newVal != null) {
                labelHorario.setText(newVal.getHorarioInicio() + " - " + newVal.getHorarioFin());

                List<LocalTime> horas = generarHorasDisponibles(newVal.getHorarioInicio(), newVal.getHorarioFin());
                boxHoraCita.getItems().addAll(horas);
                boxHoraCita.setDisable(false);
            } else {
                labelHorario.setText(" - ");
            }
        });

        // 7. Listener: Hora → habilita día
        boxHoraCita.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            pickerDiaCita.setDisable(newVal == null);
            idBotonCita.setDisable(true);
        });

        // 8. Listener: Día → habilita botón
        pickerDiaCita.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean todosLlenos = newVal != null &&
                    boxPaciente.getValue() != null &&
                    boxEspecialidad.getValue() != null &&
                    boxMedico.getValue() != null &&
                    boxHoraCita.getValue() != null;

            idBotonCita.setDisable(!todosLlenos);
        });

        boxEspecialidadActu.valueProperty().addListener((obs, oldVal, newVal) -> {
            limpiarCamposActualizar();

            if (newVal != null) {
                List<Medico> medicos = medDao.obtenerPorEspecialidad(newVal);
                boxMedicoActu.setItems(FXCollections.observableArrayList(medicos));
                boxMedicoActu.setDisable(false);
            }
        });

        boxMedicoActu.valueProperty().addListener((obs, oldVal, newVal) -> {
            boxHoraActu.getItems().clear();
            pickerDiaActu.setValue(null);
            boxHoraActu.setDisable(true);
            pickerDiaActu.setDisable(true);
            idBotonActualizarCita.setDisable(true);

            if (newVal != null) {
                labelHorarioActu.setText(newVal.getHorarioInicio() + " - " + newVal.getHorarioFin());

                List<LocalTime> horas = generarHorasDisponibles(newVal.getHorarioInicio(), newVal.getHorarioFin());
                boxHoraActu.setItems(FXCollections.observableArrayList(horas));
                boxHoraActu.setDisable(false);
            } else {
                labelHorarioActu.setText(" - ");
            }
        });

        boxHoraActu.valueProperty().addListener((obs, oldVal, newVal) -> {
            pickerDiaActu.setDisable(newVal == null);
            idBotonActualizarCita.setDisable(true);
        });

        pickerDiaActu.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean todosLlenos = newVal != null &&
                    boxEspecialidadActu.getValue() != null &&
                    boxMedicoActu.getValue() != null &&
                    boxHoraActu.getValue() != null;

            idBotonActualizarCita.setDisable(!todosLlenos);
        });

        colId.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getIdCita())));
        colMedico.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getMedico().getNombre() + " " + c.getValue().getMedico().getApellido()));
        colPaciente.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getPaciente().getNombre() + " " + c.getValue().getPaciente().getApellido()));
        colHora.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getHora().toString()));
        colFecha.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFecha().toString()));

        citas.setAll(citasDao.obtenerTodos());
        tablaCitas.setItems(citas);

        colId.setCellFactory(setColumn -> crearCeldaCentrada());
        colFecha.setCellFactory(setColumn -> crearCeldaCentrada());
        colHora.setCellFactory(setColumn -> crearCeldaCentrada());
        colMedico.setCellFactory(setColumn -> crearCeldaCentrada());
        colPaciente.setCellFactory(column -> crearCeldaCentrada());

        txtIdCita.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                buscarCitaPorId();
            }
        });

        cargarGrafico();


    }

    private void limpiarCamposActualizar() {
        boxMedicoActu.getSelectionModel().clearSelection();
        boxHoraActu.getSelectionModel().clearSelection();
        pickerDiaActu.setValue(null);
        labelHorarioActu.setText(" - ");

        boxMedicoActu.setDisable(true);
        boxHoraActu.setDisable(true);
        pickerDiaActu.setDisable(true);
        idBotonActualizarCita.setDisable(true);
    }

    private TableCell<Cita, String> crearCeldaCentrada() {
        return new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                setAlignment(Pos.CENTER);
            }
        };
    }

    private List<LocalTime> generarHorasDisponibles(LocalTime inicio, LocalTime fin) {
        List<LocalTime> horas = new ArrayList<>();
        while (!inicio.isAfter(fin.minusMinutes(30))) {
            horas.add(inicio);
            inicio = inicio.plusMinutes(30);
        }
        return horas;
    }


    @FXML
    void asignarCita(ActionEvent event) {
        Paciente paciente = boxPaciente.getValue();
        Especialidad especialidad = boxEspecialidad.getValue();
        Medico medico = boxMedico.getValue();
        LocalTime hora = boxHoraCita.getValue();
        LocalDate fecha = pickerDiaCita.getValue();

        if (paciente == null || especialidad == null || medico == null || hora == null || fecha == null) {
            AlertFactory.mostrarError("Debe completar todos los campos para asignar una cita ❌");
            return;
        }

        boolean existe = citasDao.existeCita(medico.getId(), fecha, hora);
        if (existe) {
            AlertFactory.mostrarError("Este horario ya está ocupado para el médico seleccionado ❌");
            return;
        }

        Cita nuevaCita = new Cita(fecha, hora, medico, paciente);

        int idGenerado = citasDao.insertarCita(nuevaCita);
        if (idGenerado <= 0) {
            AlertFactory.mostrarError("No se pudo guardar la cita ❌");
            return;
        }
        nuevaCita.setIdCita(idGenerado);

        double monto = EspecialidadConMonto.obtenerMontoPorNombre(medico.getEspecialidad().name());
        Factura nuevaFactura = new Factura(nuevaCita, new java.sql.Date(System.currentTimeMillis()), monto);

        FacturaDao facturaDao = new FacturaDao(Database.getConnection(), citasDao);
        facturaDao.insertar(nuevaFactura);

        AlertFactory.mostrarInfo("Éxito", "Cita y factura asignadas correctamente ✔️");

        boxPaciente.getSelectionModel().clearSelection();
        boxEspecialidad.getSelectionModel().clearSelection();
        boxMedico.getItems().clear();
        boxMedico.setDisable(true);
        boxHoraCita.getItems().clear();
        boxHoraCita.setDisable(true);
        pickerDiaCita.setValue(null);
        pickerDiaCita.setDisable(true);
        idBotonCita.setDisable(true);
        labelHorario.setText(" - ");

        citas.setAll(citasDao.obtenerTodos());
        tablaCitas.setItems(citas);
        cargarGrafico();

    }

    @FXML
    void eliminarCita(ActionEvent event) {
        Cita citaSeleccionada = tablaCitas.getSelectionModel().getSelectedItem();

        if (citaSeleccionada == null) {
            AlertFactory.mostrarError("Debe seleccionar una cita para eliminar ❌");
            return;
        }

        int idCita = citaSeleccionada.getIdCita();

        if (facturaDao.existeFacturaParaCita(idCita)) {
            AlertFactory.mostrarError("Primero debe eliminar la factura asociada a esta cita ❌");
            return;
        }

        citasDao.eliminar(idCita);
        AlertFactory.mostrarInfo("Éxito", "Cita eliminada correctamente ✔️");

        citas.setAll(citasDao.obtenerTodos());
        tablaCitas.setItems(citas);
        cargarGrafico();
    }

    @FXML
    void actualizarCita(ActionEvent event) {

        if (citaOriginal == null) {
            AlertFactory.mostrarError("❌ Debe cargar una cita primero.");
            return;
        }

        Especialidad nuevaEspecialidad = boxEspecialidadActu.getValue();
        Medico nuevoMedico = boxMedicoActu.getValue();
        LocalTime nuevaHora = boxHoraActu.getValue();
        LocalDate nuevaFecha = pickerDiaActu.getValue();

        if (nuevaEspecialidad == null || nuevoMedico == null || nuevaHora == null || nuevaFecha == null) {
            AlertFactory.mostrarError("⚠️ Complete todos los campos antes de actualizar.");
            return;
        }

        // Comparar si hubo cambios
        boolean sinCambios = nuevaEspecialidad == citaOriginal.getMedico().getEspecialidad()
                && nuevoMedico.getId().equals(citaOriginal.getMedico().getId())
                && nuevaHora.equals(citaOriginal.getHora())
                && nuevaFecha.equals(citaOriginal.getFecha());

        if (sinCambios) {
            AlertFactory.mostrarInfo("Sin cambios", "No se han detectado cambios en la cita.");
            return;
        }

        // Validar si el nuevo horario está ocupado por otro
        if (citasDao.existeCita(nuevoMedico.getId(), nuevaFecha, nuevaHora)) {
            AlertFactory.mostrarError("❌ El médico ya tiene una cita en ese horario.");
            return;
        }

        // Actualizar cita
        citaOriginal.setMedico(nuevoMedico);
        citaOriginal.setHora(nuevaHora);
        citaOriginal.setFecha(nuevaFecha);

        citasDao.actualizar(citaOriginal);
        citas.setAll(citasDao.obtenerTodos());
        tablaCitas.setItems(citas);
        cargarGrafico();

        AlertFactory.mostrarInfo("✅ Actualizado", "La cita fue actualizada correctamente.");

        // Limpiar campos
        txtIdCita.clear();
        txtPaciente.clear();
        boxEspecialidadActu.getSelectionModel().clearSelection();
        limpiarCamposActualizar();

        // Desactivar botón actualizar
        idBotonActualizarCita.setDisable(true);

        // Reset citaOriginal
        citaOriginal = null;
    }

    private void buscarCitaPorId() {
        try {
            int idBuscado = Integer.parseInt(txtIdCita.getText().trim());

            Optional<Cita> citaOpt = citasDao.obtenerTodos().stream()
                    .filter(c -> c.getIdCita() == idBuscado)
                    .findFirst();

            if (citaOpt.isEmpty()) {
                AlertFactory.mostrarError("❌ Cita no encontrada");
                return;
            }

            Cita cita = citaOpt.get();
            citaOriginal = cita;

            // 1. Cargar especialidades y seleccionar la de la cita
            boxEspecialidadActu.setItems(FXCollections.observableArrayList(Especialidad.values()));
            boxEspecialidadActu.setValue(cita.getMedico().getEspecialidad());

            // 2. Obtener todos los médicos y filtrar por especialidad
            List<Medico> medicos = medDao.obtenerPorEspecialidad(cita.getMedico().getEspecialidad());
            boxMedicoActu.setItems(FXCollections.observableArrayList(medicos));

            // 3. Seleccionar el médico correcto por ID
            for (Medico m : medicos) {
                if (m.getId().equals(cita.getMedico().getId())) {
                    boxMedicoActu.setValue(m);
                    break;
                }
            }

            // 4. Horario del médico
            Medico medicoSeleccionado = boxMedicoActu.getValue();
            if (medicoSeleccionado != null && medicoSeleccionado.getHorarioInicio() != null) {
                labelHorarioActu.setText(medicoSeleccionado.getHorarioInicio() + " - " + medicoSeleccionado.getHorarioFin());
            } else {
                labelHorarioActu.setText("Horario no disponible");
            }

            // 5. Cargar hora, fecha y paciente
            boxHoraActu.setValue(cita.getHora());
            pickerDiaActu.setValue(cita.getFecha());
            txtPaciente.setText(cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido());

        } catch (NumberFormatException e) {
            AlertFactory.mostrarError("⚠️ ID inválido");
        } catch (Exception e) {
            e.printStackTrace();
            AlertFactory.mostrarError("Error al cargar la cita");
        }
    }

    private void cargarGrafico() {
        graficoCitas.getData().clear(); // Limpiar datos anteriores

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Citas por Especialidad");

        Map<Especialidad, Long> conteo = citas.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getMedico().getEspecialidad(),
                        Collectors.counting()
                ));

        for (Map.Entry<Especialidad, Long> entry : conteo.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey().name(), entry.getValue()));
        }

        graficoCitas.getData().add(series);
    }


    @FXML
    void exportarPDF(ActionEvent event) {
        PdfExporterCita.exportarCitas(citas, (Stage) tablaCitas.getScene().getWindow());
    }
}
