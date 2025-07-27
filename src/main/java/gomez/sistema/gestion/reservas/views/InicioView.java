package gomez.sistema.gestion.reservas.views;

import gomez.sistema.gestion.reservas.dao.MedicoDao;
import gomez.sistema.gestion.reservas.dao.PacienteDao;
import gomez.sistema.gestion.reservas.sql.Database;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class InicioView {

    private final MedicoDao medDao = new MedicoDao(Database.getConnection());
    private final PacienteDao pacDao = new PacienteDao(Database.getConnection());

    @FXML
    private BorderPane mainPane;

    @FXML
    void mostrarFacturas(ActionEvent event) {

    }

    @FXML
    void mostrarCitas(ActionEvent event) {

    }

    @FXML
    void mostrarHistorial(ActionEvent event) {

    }

    public void mostrarVistaConLoader(String rutaFXML){
        try {
            FXMLLoader loaderFXML = new FXMLLoader(getClass().getResource("/loader.fxml"));
            Parent loaderRoot = loaderFXML.load();
            LoaderController controller = loaderFXML.getController();

            mainPane.setCenter(loaderRoot);

            Timeline timeline = new Timeline();
            final double[] progreso = {0};

            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50), e -> {
                progreso[0] += 0.02;
                controller.setProgress(progreso[0]);

                if (progreso[0] >= 1.0) {
                    timeline.stop();
                    try {
                        Parent vista = FXMLLoader.load(getClass().getResource(rutaFXML));

                        FadeTransition fadeIn = new FadeTransition(Duration.millis(400), vista);
                        fadeIn.setFromValue(0.0);
                        fadeIn.setToValue(1.0);
                        fadeIn.play();

                        mainPane.setCenter(vista);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }));

            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void mostrarInicio(ActionEvent event) {
        mostrarVistaConLoader("/InicioContenido.fxml");
    }

    @FXML
    void mostrarPaciente(ActionEvent event) {
        mostrarVistaConLoader("/Paciente.fxml");
    }

    @FXML
    void mostrarMedico(ActionEvent event) {
        mostrarVistaConLoader("/Medico.fxml");
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
    }
}
