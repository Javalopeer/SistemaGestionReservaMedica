package gomez.sistema.gestion.reservas.dao;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String user = dotenv.get("DB_USER");
    private static final String pass = dotenv.get("DB_PASS");
    private static final String url = dotenv.get("DB_URL");


    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(url, user, pass);
        }catch (SQLException e) {
            System.out.println("✖️ Error al conectar con la base de datos: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        Connection con = getConnection();

        if (con != null) {
            System.out.println("Conectado exitosamente a la base de datos de Reservas Medicas. ✅");

            try {
                con.close();
            }catch (SQLException e) {
                System.out.println("✖️ Error al conectar: " + e.getMessage());
            }

        }
    }






}
