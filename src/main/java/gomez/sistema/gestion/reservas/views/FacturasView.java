package gomez.sistema.gestion.reservas.views;

import gomez.sistema.gestion.reservas.dao.CitasDao;
import gomez.sistema.gestion.reservas.dao.FacturaDao;
import gomez.sistema.gestion.reservas.dao.MedicoDao;
import gomez.sistema.gestion.reservas.dao.PacienteDao;
import gomez.sistema.gestion.reservas.entities.Factura;
import gomez.sistema.gestion.reservas.error.AlertFactory;
import gomez.sistema.gestion.reservas.pdf.PdfExporterFactura;
import gomez.sistema.gestion.reservas.sql.Database;
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

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FacturasView {

    private final MedicoDao medDao = new MedicoDao(gomez.sistema.gestion.reservas.dao.Database.getConnection());
    private final PacienteDao pacDao = new PacienteDao(gomez.sistema.gestion.reservas.dao.Database.getConnection());
    private final CitasDao citasDao = new CitasDao(gomez.sistema.gestion.reservas.dao.Database.getConnection(), pacDao, medDao);
    private final FacturaDao facturas = new FacturaDao(gomez.sistema.gestion.reservas.dao.Database.getConnection(), citasDao);
    private ObservableList<Factura> facturasLista = FXCollections.observableArrayList();

    @FXML private TableView<Factura> tablaFacturas;
    @FXML private TableColumn<Factura, Integer> colId;
    @FXML private TableColumn<Factura, String> colPaciente, colMedico, colEspecialidad;
    @FXML private TableColumn<Factura, Double> colMonto;
    @FXML private TableColumn<Factura, java.sql.Date> colFecha;
    @FXML private Label lblTotalFacturas;
    @FXML private Label lblMontoTotal;
    @FXML private ComboBox<String> cmbFiltroPaciente;
    @FXML private DatePicker dateFiltro;
    @FXML private BarChart<String, Number> graficoComparativo;
    @FXML private CategoryAxis ejeXComparativo;
    @FXML private NumberAxis ejeYComparativo;


    private FacturaDao facturaDao;


    @FXML
    public void initialize() {
        try (Connection con = Database.getConnection()) {
            if (con != null) {
                System.out.println("Conectado exitosamente a la base de datos de Reservas Medicas. ✅");
            }
        } catch (SQLException e) {
            System.out.println("✖️ Error al conectar: " + e.getMessage());
        }
        PacienteDao pacienteDao = new PacienteDao(Database.getConnection());
        MedicoDao medicoDao = new MedicoDao(Database.getConnection());
        CitasDao citasDao = new CitasDao(Database.getConnection(), pacienteDao, medicoDao);
        facturaDao = new FacturaDao(Database.getConnection(), citasDao);

        colEspecialidad.setCellFactory(setColumn -> crearCeldaCentrada());
        colPaciente.setCellFactory(setColumn -> crearCeldaCentrada());
        colId.setCellFactory(setColumn -> crearCeldaCentradaInteger());
        colMedico.setCellFactory(setColumn -> crearCeldaCentrada());
        colMonto.setCellFactory(setColumn -> crearCeldaCentradaDouble());
        colFecha.setCellFactory(setColumn -> crearCeldaCentradaDate());

        configurarColumnas();
        cargarFacturas();
        cargarGraficoComparativo();
    }

    private TableCell<Factura, String> crearCeldaCentrada() {
        return new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                setAlignment(Pos.CENTER);
            }
        };
    }
    private TableCell<Factura, Double> crearCeldaCentradaDouble() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("₡%.2f", item));
                setAlignment(Pos.CENTER);
            }
        };
    }
    private TableCell<Factura, Integer> crearCeldaCentradaInteger() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.valueOf(item));
                setAlignment(Pos.CENTER);
            }
        };
    }
    private TableCell<Factura, java.sql.Date> crearCeldaCentradaDate() {
        return new TableCell<>() {
            @Override
            protected void updateItem(java.sql.Date item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
                setAlignment(Pos.CENTER);
            }
        };
    }
    private void configurarColumnas() {
        colId.setCellValueFactory(f -> new javafx.beans.property.SimpleIntegerProperty(f.getValue().getIdFactura()).asObject());
        colPaciente.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().getCita().getPaciente().getNombre()));
        colMedico.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().getCita().getMedico().getNombre()));
        colEspecialidad.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().getCita().getMedico().getEspecialidad().name()));
        colMonto.setCellValueFactory(f -> new javafx.beans.property.SimpleDoubleProperty(f.getValue().getMonto()).asObject());
        colFecha.setCellValueFactory(f -> new javafx.beans.property.SimpleObjectProperty<>(f.getValue().getFechaEmision()));
    }

    @FXML
    private void cargarFacturas() {
        // Limpiar controles visuales
        cmbFiltroPaciente.getSelectionModel().clearSelection();
        dateFiltro.setValue(null);

        // Volver a cargar todas las facturas
        List<Factura> lista = facturaDao.obtenerTodos();
        facturasLista.setAll(lista);
        tablaFacturas.setItems(facturasLista);

        // Volver a poblar el ComboBox
        cmbFiltroPaciente.setItems(FXCollections.observableArrayList(
                lista.stream()
                        .map(f -> f.getCita().getPaciente().getNombre())
                        .distinct()
                        .sorted()
                        .toList()
        ));

        actualizarResumen();
    }

    private void actualizarResumen() {
        int total = facturasLista.size();
        double monto = facturasLista.stream().mapToDouble(Factura::getMonto).sum();
        lblTotalFacturas.setText("Total: " + total);
        lblMontoTotal.setText(String.format("Monto Total: ₡%,.2f", monto));
    }

    private void cargarGraficoComparativo() {
        // Serie 1: por médico
        Map<String, Long> porMedico = facturasLista.stream()
                .collect(Collectors.groupingBy(f -> f.getCita().getMedico().getNombre(), Collectors.counting()));

        XYChart.Series<String, Number> serieMedicos = new XYChart.Series<>();
        serieMedicos.setName("Por Médico");
        porMedico.forEach((medico, cantidad) -> serieMedicos.getData().add(new XYChart.Data<>(medico, cantidad)));

        // Serie 2: por especialidad
        Map<String, Long> porEspecialidad = facturasLista.stream()
                .collect(Collectors.groupingBy(f -> f.getCita().getMedico().getEspecialidad().name(), Collectors.counting()));

        XYChart.Series<String, Number> serieEspecialidades = new XYChart.Series<>();
        serieEspecialidades.setName("Por Especialidad");
        porEspecialidad.forEach((esp, cantidad) -> serieEspecialidades.getData().add(new XYChart.Data<>(esp, cantidad)));

        // Mostrar ambas
        graficoComparativo.getData().clear();
        graficoComparativo.getData().addAll(serieMedicos, serieEspecialidades);
    }

    @FXML
    private void filtrarFacturas() {
        String paciente = cmbFiltroPaciente.getValue();
        LocalDate fecha = dateFiltro.getValue();

        List<Factura> lista = facturaDao.obtenerTodos().stream()
                .filter(f -> (paciente == null || f.getCita().getPaciente().getNombre().equals(paciente)))
                .filter(f -> (fecha == null || f.getFechaEmision().toLocalDate().equals(fecha)))
                .toList();

        facturasLista.setAll(lista);
        tablaFacturas.setItems(facturasLista);
        actualizarResumen();
    }

    @FXML
    void eliminarFactura(ActionEvent event) {
        Factura facturaSeleccionada = tablaFacturas.getSelectionModel().getSelectedItem();

        if (facturaSeleccionada == null) {
            AlertFactory.mostrarError("Debe seleccionar una factura para eliminar ❌");
            return;
        }

        facturaDao.eliminar(facturaSeleccionada.getIdFactura());
        AlertFactory.mostrarInfo("Éxito", "Factura eliminada correctamente ✔️");

        facturasLista.setAll(facturaDao.obtenerTodos()); // ✅ Ahora funciona
        tablaFacturas.setItems(facturasLista);
    }

    @FXML
    private void exportarFacturaPDF() {
        Factura seleccionada = tablaFacturas.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            PdfExporterFactura.exportarFactura(seleccionada);
        } else {
            AlertFactory.mostrarError("Selecciona una factura para exportar.");
        }
    }
}
