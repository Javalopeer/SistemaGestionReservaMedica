package gomez.sistema.gestion.reservas;


import gomez.sistema.gestion.reservas.utilities.Path;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {

    public static void main(String[] args) {
        launch();



    }


    @Override
    public void start(Stage stage) throws Exception {

        AnchorPane load = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Path.MEDICO_CONTROLLER)));
        Scene scene = new Scene(load);
        stage.setScene(scene);
        stage.show();

    }
}
