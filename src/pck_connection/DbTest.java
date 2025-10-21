package pck_connection;

import java.sql.Connection;


public class DbTest {

    public static void main(String[] args) {
        Connection cn = pck_connection.DbConnection.getConnection();
        if (cn != null) {
            System.out.println("¡Conexión OK!");
            pck_connection.DbConnection.close(cn);
        } else {
            System.out.println("No se pudo conectar.");
        }
    }
}
