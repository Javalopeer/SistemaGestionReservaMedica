package gomez.sistema.gestion.reservas.views;

import gomez.sistema.gestion.reservas.dao.MedicoDao;
import gomez.sistema.gestion.reservas.dao.PacienteDao;
import gomez.sistema.gestion.reservas.sql.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class InicioView {

    private final MedicoDao medDao = new MedicoDao(Database.getConnection());
    private final PacienteDao pacDao = new PacienteDao(Database.getConnection());

    @FXML
    private ToggleButton btnInicio;

    @FXML
    private Label cantidadCitas;

    @FXML
    private Label cantidadMedicos;

    @FXML
    private Label cantidadPacientes;

    @FXML
    private BorderPane mainPane;

    @FXML
    private PieChart pieChartCitas;

    @FXML
    void mostrarFacturas(ActionEvent event) {

    }

    @FXML
    public void mostrarInicio(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/InicioContenido.fxml"));
            System.out.println(getClass().getResource("/InicioContenido.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            mainPane.setCenter(root);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void mostrarPaciente(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Paciente.fxml"));
            System.out.println(getClass().getResource("/Paciente.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            mainPane.setCenter(root);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void mostrarMedico(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Medico.fxml"));
            System.out.println(getClass().getResource("/Medico.fxml"));
            Parent root = loader.load();
            mainPane.setCenter(root);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    @FXML
    void initialize(){
        try(Connection con = Database.getConnection()) {
            if (con != null) {
                System.out.println("Conectado exitosamente a la base de datos de Reservas Medicas. ✅");
            }
        }catch (SQLException e) {
            System.out.println("✖️ Error al conectar: " + e.getMessage());
        }

//        List<Medico> medicos = medDao.obtenerTodos();
//        for (Medico m : medicos){
//            System.out.println(m.getNombre() + " - " + m.getEspecialidad());
//        }
//
//        List<Paciente> pacientes = pacDao.obtenerTodos();
//        for (Paciente p : pacientes){
//            System.out.println(p.getCedula() + " - " + p.getApellido() + ", " + p.getNombre());
//        }
//
//        Map<String, Integer> conteo = new HashMap<>();
//        for (Medico m : medicos) {
//            String esp = m.getEspecialidad().name();
//            conteo.put(esp, conteo.getOrDefault(esp, 0) + 1);
//        }
//
//        cantidadMedicos.setText("" + medicos.size());
//        cantidadPacientes.setText("" + pacientes.size());
//        pieChartCitas.getData().clear();
//        for (Map.Entry<String, Integer> entrada : conteo.entrySet()) {
//            pieChartCitas.getData().add(new PieChart.Data(entrada.getKey(), entrada.getValue()));
//        }
//        for (PieChart.Data data : pieChartCitas.getData()) {
//            double total = pieChartCitas.getData().stream()
//                    .mapToDouble(PieChart.Data::getPieValue)
//                    .sum();
//
//            String porcentaje = String.format("%.1f%%", (data.getPieValue() / total) * 100);
//            Tooltip.install(data.getNode(), new Tooltip(data.getName() + ": " + porcentaje));
//        }

    }
}
