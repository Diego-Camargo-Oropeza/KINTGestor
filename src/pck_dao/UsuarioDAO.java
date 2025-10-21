package pck_dao;

import pck_connection.DbConnection;
import pck_model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    private static final String SQL_FIND_BY_CORREO
            = "SELECT u.id_usuario, u.id_rol, u.nombre, u.correo, u.contrasena_hash, u.activo, r.nombre AS rol_nombre "
            + "FROM usuario u INNER JOIN rol r ON r.id_rol = u.id_rol "
            + "WHERE u.correo = ? AND u.activo = 1";

    private static final String SQL_INSERT
            = "INSERT INTO usuario (id_rol, nombre, correo, contrasena_hash, activo) VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE_PASSWORD
            = "UPDATE usuario SET contrasena_hash = ? WHERE id_usuario = ?";

    private static final String SQL_SET_ACTIVO
            = "UPDATE usuario SET activo = ? WHERE id_usuario = ?";

    public Usuario findByCorreo(String correo) {
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario u = null;

        try {
            cn = DbConnection.getConnection();
            if (cn == null) {
                return null;
            }

            ps = cn.prepareStatement(SQL_FIND_BY_CORREO);
            ps.setString(1, correo);
            rs = ps.executeQuery();

            if (rs.next()) {
                u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setIdRol(rs.getInt("id_rol"));
                u.setNombre(rs.getString("nombre"));
                u.setCorreo(rs.getString("correo"));
                u.setContrasenaHash(rs.getString("contrasena_hash"));
                u.setActivo(rs.getInt("activo") == 1);
                u.setRolNombre(rs.getString("rol_nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (ps != null) try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DbConnection.close(cn);
        }
        return u;
    }

    public boolean insert(Usuario u) {
        Connection cn = null;
        PreparedStatement ps = null;
        int rows = 0;

        try {
            cn = DbConnection.getConnection();
            if (cn == null) {
                return false;
            }

            ps = cn.prepareStatement(SQL_INSERT);
            ps.setInt(1, u.getIdRol());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getContrasenaHash());
            ps.setInt(5, u.isActivo() ? 1 : 0);

            rows = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DbConnection.close(cn);
        }
        return rows == 1;
    }

    public boolean updatePassword(int idUsuario, String nuevoHash) {
        Connection cn = null;
        PreparedStatement ps = null;
        int rows = 0;

        try {
            cn = DbConnection.getConnection();
            if (cn == null) {
                return false;
            }

            ps = cn.prepareStatement(SQL_UPDATE_PASSWORD);
            ps.setString(1, nuevoHash);
            ps.setInt(2, idUsuario);

            rows = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DbConnection.close(cn);
        }
        return rows == 1;
    }

    public boolean setActivo(int idUsuario, boolean activo) {
        Connection cn = null;
        PreparedStatement ps = null;
        int rows = 0;

        try {
            cn = DbConnection.getConnection();
            if (cn == null) {
                return false;
            }

            ps = cn.prepareStatement(SQL_SET_ACTIVO);
            ps.setInt(1, activo ? 1 : 0);
            ps.setInt(2, idUsuario);

            rows = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DbConnection.close(cn);
        }
        return rows == 1;
    }
}
