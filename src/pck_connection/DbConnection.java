package pck_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB = "kint_inv";
    private static final String USER = "root";    
    private static final String PASS = "";       
    private static final String URL =
        "jdbc:mysql://localhost:3306/" + DB + "?useSSL=false&serverTimezone=UTC";

    public static Connection getConnection() {
        Connection cnx = null;
        try {
            Class.forName(DRIVER); // carga el driver
            cnx = DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró el driver MySQL.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error al conectar a la BD.");
            e.printStackTrace();
        }
        return cnx;
    }

    public static void close(Connection cnx) {
        if (cnx != null) {
            try { cnx.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
