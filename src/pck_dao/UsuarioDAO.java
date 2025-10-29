package pck_dao;

import pck_connection.DbConnection;
import pck_model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // ========== SQL ==========
    private static final String SQL_BASE_SELECT
            = "SELECT u.id_usuario, u.id_rol, u.nombre, u.correo, u.contrasena_hash, "
            + "       u.activo, u.tarea, u.fecha_nacimiento, r.nombre AS rol_nombre "
            + "FROM usuario u INNER JOIN rol r ON r.id_rol = u.id_rol ";

    private static final String SQL_FIND_ALL
            = SQL_BASE_SELECT + "ORDER BY u.id_usuario";

    private static final String SQL_FIND_BY_ID
            = SQL_BASE_SELECT + "WHERE u.id_usuario = ?";

    private static final String SQL_FIND_BY_CORREO
            = SQL_BASE_SELECT + "WHERE u.correo = ? AND u.activo = 1";

    private static final String SQL_INSERT
            = "INSERT INTO usuario (id_rol, nombre, correo, contrasena_hash, activo, tarea, fecha_nacimiento) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE
            = "UPDATE usuario SET id_rol = ?, nombre = ?, correo = ?, tarea = ?, activo = ?, fecha_nacimiento = ? "
            + "WHERE id_usuario = ?";

    private static final String SQL_UPDATE_PASSWORD
            = "UPDATE usuario SET contrasena_hash = ? WHERE id_usuario = ?";

    private static final String SQL_SET_ACTIVO
            = "UPDATE usuario SET activo = ? WHERE id_usuario = ?";

    private static final String SQL_DELETE_HARD
            = "DELETE FROM usuario WHERE id_usuario = ?";

    private static final String SQL_MAIL_DUPPED = "SELECT 1 FROM usuario WHERE correo = ? AND id_usuario <> ? LIMIT 1";

    // ========== CONVERSIÓN FECHAS ==========
    // java.util.Date a java.sql.Date
    private Date toSqlDate(java.util.Date d) {
        if (d == null) {
            return null;
        }
        return new Date(d.getTime());
    }

    // ========== MAPEO ==========
    private Usuario mapUsuario(ResultSet rs) {
        try {
            Usuario u = new Usuario();
            u.setIdUsuario(rs.getInt("id_usuario"));
            u.setIdRol(rs.getInt("id_rol"));
            u.setNombre(rs.getString("nombre"));
            u.setCorreo(rs.getString("correo"));
            u.setContrasenaHash(rs.getString("contrasena_hash"));
            u.setActivo(rs.getInt("activo") == 1);
            u.setTarea(rs.getString("tarea"));

            Date fn = rs.getDate("fecha_nacimiento");
            u.setFechaNacimiento(fn == null ? null : new java.util.Date(fn.getTime()));

            u.setRolNombre(rs.getString("rol_nombre"));
            return u;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // ========== QUERIES ==========
    public List<Usuario> findAll() {
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Usuario> lista = new ArrayList<>();

        try {
            cn = DbConnection.getConnection();
            if (cn == null) {
                return lista;
            }

            ps = cn.prepareStatement(SQL_FIND_ALL);
            rs = ps.executeQuery();

            while (rs.next()) {
                Usuario u = mapUsuario(rs);
                if (u != null) {
                    lista.add(u);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            DbConnection.close(cn);
        }
        return lista;
    }

    public Usuario findById(int idUsuario) {
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario u = null;

        try {
            cn = DbConnection.getConnection();
            if (cn == null) {
                return null;
            }

            ps = cn.prepareStatement(SQL_FIND_BY_ID);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();

            if (rs.next()) {
                u = mapUsuario(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            DbConnection.close(cn);
        }

        return u;
    }

    public boolean existsCorreoForOther(String correo, int idUsuario) {
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean existe = false;

        try {
            cn = DbConnection.getConnection();
            if (cn == null) {
                return false;
            }

            ps = cn.prepareStatement(SQL_MAIL_DUPPED);
            ps.setString(1, correo);
            ps.setInt(2, idUsuario);
            rs = ps.executeQuery();
            existe = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException ignored) {
            }
            if (ps != null) try {
                ps.close();
            } catch (SQLException ignored) {
            }
            DbConnection.close(cn);
        }
        return existe;
    }

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
                u = mapUsuario(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            DbConnection.close(cn);
        }

        return u;
    }

    // ========== INSERTS ==========
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
            ps.setString(6, u.getTarea());
            ps.setDate(7, toSqlDate(u.getFechaNacimiento()));

            rows = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            DbConnection.close(cn);
        }

        return rows == 1;
    }

    public int insertReturningId(Usuario u) {
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int newId = 0;

        try {
            cn = DbConnection.getConnection();
            if (cn == null) {
                return 0;
            }

            ps = cn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, u.getIdRol());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getContrasenaHash());
            ps.setInt(5, u.isActivo() ? 1 : 0);
            ps.setString(6, u.getTarea());
            ps.setDate(7, toSqlDate(u.getFechaNacimiento()));

            int rows = ps.executeUpdate();
            if (rows == 1) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    newId = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            DbConnection.close(cn);
        }

        return newId;
    }

    // ========== UPDATES ==========
    public boolean update(Usuario u) {
        Connection cn = null;
        PreparedStatement ps = null;
        int rows = 0;

        try {
            cn = DbConnection.getConnection();
            if (cn == null) {
                return false;
            }

            ps = cn.prepareStatement(SQL_UPDATE);
            ps.setInt(1, u.getIdRol());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getTarea());
            ps.setInt(5, u.isActivo() ? 1 : 0);
            if (u.getFechaNacimiento() != null) {
                ps.setDate(6, new java.sql.Date(u.getFechaNacimiento().getTime()));
            } else {
                ps.setNull(6, java.sql.Types.DATE);
            }
            ps.setInt(7, u.getIdUsuario());

            rows = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) try {
                ps.close();
            } catch (SQLException ignored) {
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
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
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
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            DbConnection.close(cn);
        }

        return rows == 1;
    }

    // ========== DELETE ==========
    public boolean deleteHard(int idUsuario) {
        Connection cn = null;
        PreparedStatement ps = null;
        int rows = 0;

        try {
            cn = DbConnection.getConnection();
            if (cn == null) {
                return false;
            }

            ps = cn.prepareStatement(SQL_DELETE_HARD);
            ps.setInt(1, idUsuario);

            rows = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            DbConnection.close(cn);
        }

        return rows == 1;
    }
}
