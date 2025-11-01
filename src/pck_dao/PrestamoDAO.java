package pck_dao;

import pck_connection.DbConnection;
import pck_model.PrestamoRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PrestamoDAO {

    // ========================================================================
    // SQL
    // ========================================================================
    private static final String SQL_ALL
            = "SELECT s.id_solicitud, s.folio, p.nombre AS producto, d.cantidad, "
            + "       s.estado, s.prioridad, s.fecha_solicitud AS fecha_creacion "
            + "FROM solicitud s "
            + "JOIN detalle d   ON d.id_solicitud = s.id_solicitud "
            + "JOIN producto p  ON p.id_producto  = d.id_producto "
            + "ORDER BY s.fecha_solicitud DESC, s.id_solicitud DESC";

    private static final String SQL_BY_USER
            = "SELECT s.id_solicitud, s.folio, p.nombre AS producto, d.cantidad, "
            + "       s.estado, s.prioridad, s.fecha_solicitud AS fecha_creacion "
            + "FROM solicitud s "
            + "JOIN detalle d   ON d.id_solicitud = s.id_solicitud "
            + "JOIN producto p  ON p.id_producto  = d.id_producto "
            + "WHERE s.id_usuario = ? "
            + "ORDER BY s.fecha_solicitud DESC, s.id_solicitud DESC";

    private static final String SQL_PENDIENTES
            = "SELECT s.id_solicitud, s.folio, p.nombre AS producto, d.cantidad, "
            + "       s.estado, s.prioridad, s.fecha_solicitud AS fecha_creacion "
            + "FROM solicitud s "
            + "JOIN detalle d   ON d.id_solicitud = s.id_solicitud "
            + "JOIN producto p  ON p.id_producto  = d.id_producto "
            + "WHERE s.estado = 'ENVIADA' "
            + "ORDER BY s.fecha_solicitud DESC, s.id_solicitud DESC";

    // Crea una solicitud: por defecto estado ENVIADA, fecha_solicitud = NOW()
    private static final String SQL_INSERT_SOLICITUD
            = "INSERT INTO solicitud (id_usuario, folio, estado, prioridad, comentarios, fecha_solicitud) "
            + "VALUES (?, ?, ?, ?, ?, NOW())";

    // Aprueba/Rechaza solicitud. Si pasa a APROBADA o RECHAZADA, fija fecha_aprobacion = NOW()
    private static final String SQL_UPDATE_ESTADO
            = "UPDATE solicitud "
            + "SET estado = ?, comentarios = ?, fecha_aprobacion = CASE "
            + "       WHEN ? IN ('APROBADA','RECHAZADA') THEN NOW() "
            + "       ELSE fecha_aprobacion END "
            + "WHERE id_solicitud = ?";

    // ========================================================================
    // Listados
    // ========================================================================
    public List<PrestamoRow> listAll() {
        List<PrestamoRow> out = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            cn = DbConnection.getConnection();
            if (cn == null) {
                return out;
            }

            ps = cn.prepareStatement(SQL_ALL);
            rs = ps.executeQuery();
            while (rs.next()) {
                out.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeQuiet(rs);
            closeQuiet(ps);
            DbConnection.close(cn);
        }
        return out;
    }

    public List<PrestamoRow> listByUsuario(int idUsuario) {
        List<PrestamoRow> out = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            cn = DbConnection.getConnection();
            if (cn == null) {
                return out;
            }

            ps = cn.prepareStatement(SQL_BY_USER);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            while (rs.next()) {
                out.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeQuiet(rs);
            closeQuiet(ps);
            DbConnection.close(cn);
        }
        return out;
    }

    public List<PrestamoRow> listPendientes() {
        List<PrestamoRow> out = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            cn = DbConnection.getConnection();
            if (cn == null) {
                return out;
            }

            ps = cn.prepareStatement(SQL_PENDIENTES);
            rs = ps.executeQuery();
            while (rs.next()) {
                out.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeQuiet(rs);
            closeQuiet(ps);
            DbConnection.close(cn);
        }
        return out;
    }

    // ========================================================================
    // Escrituras
    // ========================================================================
    /**
     * Inserta encabezado de solicitud. Devuelve true si insertó 1 fila.
     */
    public boolean crearSolicitud(int idUsuario, String folio, String prioridad, String comentarios) {
        Connection cn = null;
        PreparedStatement ps = null;
        int rows = 0;

        try {
            cn = DbConnection.getConnection();
            if (cn == null) {
                return false;
            }

            ps = cn.prepareStatement(SQL_INSERT_SOLICITUD);
            ps.setInt(1, idUsuario);
            ps.setString(2, n(folio));
            ps.setString(3, "ENVIADA");
            ps.setString(4, n(prioridad));   // BAJA | MEDIA | ALTA
            ps.setString(5, n(comentarios));

            rows = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeQuiet(ps);
            DbConnection.close(cn);
        }
        return rows == 1;
    }

    /**
     * Cambia el estado de la solicitud. nuevoEstado: ENVIADA | APROBADA |
     * RECHAZADA | CANCELADA ... Si es APROBADA o RECHAZADA, fija
     * fecha_aprobacion = NOW().
     */
    public boolean actualizarEstado(int idSolicitud, String nuevoEstado, String comentarios) {
        Connection cn = null;
        PreparedStatement ps = null;
        int rows = 0;

        try {
            cn = DbConnection.getConnection();
            if (cn == null) {
                return false;
            }

            ps = cn.prepareStatement(SQL_UPDATE_ESTADO);
            ps.setString(1, n(nuevoEstado));
            ps.setString(2, n(comentarios));
            ps.setString(3, n(nuevoEstado));   // para el CASE del mismo UPDATE
            ps.setInt(4, idSolicitud);

            rows = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeQuiet(ps);
            DbConnection.close(cn);
        }
        return rows == 1;
    }

    // ========================================================================
    // Mapeo y utilidades
    // ========================================================================
    private PrestamoRow mapRow(ResultSet rs) {
        PrestamoRow r = new PrestamoRow();
        try {
            r.setIdSolicitud(rs.getInt("id_solicitud"));
            r.setFolio(rs.getString("folio"));
            r.setProducto(rs.getString("producto"));
            r.setCantidad(rs.getInt("cantidad"));
            r.setEstado(rs.getString("estado"));
            r.setPrioridad(rs.getString("prioridad"));

            Timestamp ts = rs.getTimestamp("fecha_creacion"); // alias de fecha_solicitud
            r.setFechaCreacion(ts);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    private static void closeQuiet(AutoCloseable c) {
        if (c != null) {
            try {
                c.close();
            } catch (Exception ignored) {
            }
        }
    }

    private static String n(String s) {
        return (s == null) ? "" : s.trim();
    }
}
