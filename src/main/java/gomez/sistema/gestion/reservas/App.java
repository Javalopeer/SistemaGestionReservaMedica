package gomez.sistema.gestion.reservas;


import gomez.sistema.gestion.reservas.utils.Path;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {
     @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(Path.MEDICO_CONTROLLER));

        BorderPane pane = loader.load();

        Scene scene = new Scene(pane);

        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();



    }
}
