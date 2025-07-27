package gomez.sistema.gestion.reservas.views;

import gomez.sistema.gestion.reservas.dao.CitasDao;
import gomez.sistema.gestion.reservas.dao.MedicoDao;
import gomez.sistema.gestion.reservas.dao.PacienteDao;
import gomez.sistema.gestion.reservas.entities.Cita;
import gomez.sistema.gestion.reservas.entities.Medico;
import gomez.sistema.gestion.reservas.entities.Paciente;
import gomez.sistema.gestion.reservas.sql.Database;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContenidoView {

    private final MedicoDao medDao = new MedicoDao(Database.getConnection());
    private final PacienteDao pacDao = new PacienteDao(Database.getConnection());
    private final CitasDao citDao = new CitasDao(Database.getConnection());


    @FXML
    private Label cantidadCitas;

    @FXML
    private Label cantidadMedicos;

    @FXML
    private Label cantidadPacientes;

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

        List<Paciente> pacientes = pacDao.obtenerTodos();
        for (Paciente p : pacientes){
            System.out.println(p.getCedula() + " - " + p.getApellido() + ", " + p.getNombre());
        }

        List<Cita> citas = citDao.obtenerTodos();
        for (Cita c : citas){
            System.out.println(c.getMedico() + " - " + c.getPaciente() + " - " + c.getHora() + " - " + c.getFecha());
        }

        Map<String, Integer> conteo = new HashMap<>();
        for (Medico m : medicos) {
            String esp = m.getEspecialidad().name();
            conteo.put(esp, conteo.getOrDefault(esp, 0) + 1);
        }

        cantidadMedicos.setText("" + medicos.size());
        cantidadPacientes.setText("" + pacientes.size());
        cantidadCitas.setText("" + citas.size());
        pieChartCitas.getData().clear();
        for (Map.Entry<String, Integer> entrada : conteo.entrySet()) {
            pieChartCitas.getData().add(new PieChart.Data(entrada.getKey(), entrada.getValue()));
        }
        for (PieChart.Data data : pieChartCitas.getData()) {
            double total = pieChartCitas.getData().stream()
                    .mapToDouble(PieChart.Data::getPieValue)
                    .sum();

            String porcentaje = String.format("%.1f%%", (data.getPieValue() / total) * 100);
            Tooltip.install(data.getNode(), new Tooltip(data.getName() + ": " + porcentaje));
        }

    }

}
