package gomez.sistema.gestion.reservas.sql;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String URL = dotenv.get("DB_URL");
    private static final String USERNAME = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASS");

    public static Connection getConnection() {
        try{
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("❌ Error al conectar: " + e.getMessage());
            return null;
        }
    }

    public void verificarConexion() {
        Connection con = getConnection();

        if (con != null) {
            System.out.println("Conectado a la base de datos. ✅");

            try {
                con.close();
            } catch (Exception e) {
                System.out.println("✖️ Error al conectar: " + e.getMessage());
            }
        }
    }



}
