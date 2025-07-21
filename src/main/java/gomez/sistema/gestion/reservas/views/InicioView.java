package gomez.sistema.gestion.reservas.views;

import gomez.sistema.gestion.reservas.dao.Database;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;

import java.sql.Connection;
import java.sql.SQLException;

public class InicioView {

    @FXML
    private ToggleButton btnInicio;


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
