package gomez.sistema.gestion.reservas.views;

import gomez.sistema.gestion.reservas.dao.MedicoDao;
import gomez.sistema.gestion.reservas.entities.Medico;
import gomez.sistema.gestion.reservas.sql.Database;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class InicioView {

    private final MedicoDao medDao = new MedicoDao(Database.getConnection());

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
    void initialize(){
        try(Connection con = Database.getConnection()) {
            if (con != null) {
                System.out.println("Conectado exitosamente a la base de datos de Reservas Medicas. ✅");
            }
        }catch (SQLException e) {
            System.out.println("✖️ Error al conectar: " + e.getMessage());
        }

        List<Medico> medicos = medDao.obtenerTodos();
        for (Medico m : medicos){
            System.out.println(m.getNombre() + " - " + m.getEspecialidad());
        }

        cantidadMedicos.setText("" + medicos.size());

    }
}
