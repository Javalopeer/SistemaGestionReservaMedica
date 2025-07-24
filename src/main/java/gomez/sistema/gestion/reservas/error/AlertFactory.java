package gomez.sistema.gestion.reservas.error;

import javafx.scene.control.Alert;

public class AlertFactory {


    public static void mostrarInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exito");
        alert.setHeaderText("Ha tenido exito");
        alert.setContentText(mensaje);
        alert.showAndWait();



    }

    public static void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Algo salió mal");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
