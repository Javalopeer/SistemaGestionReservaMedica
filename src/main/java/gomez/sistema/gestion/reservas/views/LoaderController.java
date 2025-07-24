package gomez.sistema.gestion.reservas.views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class LoaderController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressLabel;

    public void setProgress(double value){
        progressBar.setProgress(value);

        int porcentaje = (int) (value * 100);
        progressLabel.setText("Cargando... " + porcentaje + "%");
    }

}
