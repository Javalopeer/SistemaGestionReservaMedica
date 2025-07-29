package gomez.sistema.gestion.reservas;

import gomez.sistema.gestion.reservas.utils.Path;
import gomez.sistema.gestion.reservas.views.InicioView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {
     @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(Path.INICIO_CONTROLLER));

        BorderPane pane = loader.load();

        InicioView controller = loader.getController();
        controller.mostrarInicio(null);

        Scene scene = new Scene(pane);

        stage.setScene(scene);
        stage.setTitle("Sistema de Gestión de Reservas Médicas");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/clinic.png"))));
        stage.show();

    }

    public static void main(String[] args) {
        launch();



    }
}
