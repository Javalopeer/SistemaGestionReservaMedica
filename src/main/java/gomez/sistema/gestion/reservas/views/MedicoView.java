package gomez.sistema.gestion.reservas.views;

import gomez.sistema.gestion.reservas.controllers.MedicoController;
import gomez.sistema.gestion.reservas.entities.Especialidad;
import gomez.sistema.gestion.reservas.dao.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;

import java.sql.Connection;
import java.sql.SQLException;

public class MedicoView {

    private final MedicoController controller = new MedicoController();

    @FXML
    private BorderPane borderPane;

    @FXML
    private ToggleButton btnInicio;

    @FXML
    private Label lblAccion;

     @FXML
    void initialize(){
         try(Connection con = Database.getConnection()) {
             if (con != null) {
                 System.out.println("Conectado exitosamente a la base de datos de Reservas Medicas. ✅");
             }
         }catch (SQLException e) {
             System.out.println("✖️ Error al conectar: " + e.getMessage());
         }
//         comboEspecialidad.getItems().addAll(Especialidad.values());
//
//         comboEspecialidad.setConverter(new StringConverter<Especialidad>() {
//             @Override
//             public String toString(Especialidad especialidad) {
//                 return especialidad != null ? especialidad.name() : "";
//             }
//
//             @Override
//             public Especialidad fromString(String string) {
//                 return Especialidad.valueOf(string);
//             }
//         });
     }


}
