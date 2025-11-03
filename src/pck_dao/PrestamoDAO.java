package pck_dao;

import pck_connection.DbConnection;
import pck_model.PrestamoRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    public String crearSolicitudConDetalle(int idUsuario, int idProducto, int cantidad, String prioridad, String comentarios) {
        String folioGenerado = null;

        String SQL_INSERT_SOLICITUD
                = "INSERT INTO solicitud (id_usuario, folio, estado, prioridad, comentarios, fecha_solicitud) "
                + "VALUES (?, ?, 'ENVIADA', ?, ?, NOW())";

        String SQL_INSERT_DETALLE
                = "INSERT INTO detalle (id_solicitud, id_producto, cantidad) VALUES (?, ?, ?)";

        Connection cn = null;
        PreparedStatement psSol = null;
        PreparedStatement psDet = null;
        ResultSet rsKeys = null;

        try {
            cn = DbConnection.getConnection();
            if (cn == null) {
                return null;
            }

            cn.setAutoCommit(false);

            // 1) Generar folio simple (puedes reemplazarlo por tu generador real)
            String folio = generarFolioSolicitud(cn); // e.g. "SOL20251102-000123"

            // 2) Insert encabezado
            psSol = cn.prepareStatement(SQL_INSERT_SOLICITUD, Statement.RETURN_GENERATED_KEYS);
            psSol.setInt(1, idUsuario);
            psSol.setString(2, folio);
            psSol.setString(3, safe(prioridad));
            psSol.setString(4, safe(comentarios));
            int rows1 = psSol.executeUpdate();

            if (rows1 != 1) {
                cn.rollback();
                return null;
            }

            rsKeys = psSol.getGeneratedKeys();
            int idSolicitud;
            if (rsKeys.next()) {
                idSolicitud = rsKeys.getInt(1);
            } else {
                cn.rollback();
                return null;
            }

            // 3) Insert detalle
            psDet = cn.prepareStatement(SQL_INSERT_DETALLE);
            psDet.setInt(1, idSolicitud);
            psDet.setInt(2, idProducto);
            psDet.setInt(3, cantidad);
            int rows2 = psDet.executeUpdate();

            if (rows2 != 1) {
                cn.rollback();
                return null;
            }

            cn.commit();
            folioGenerado = folio;
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                if (cn != null) {
                    cn.rollback();
                }
            } catch (Exception ignored) {
            }
        } finally {
            try {
                if (rsKeys != null) {
                    rsKeys.close();
                }
            } catch (Exception ignored) {
            }
            try {
                if (psDet != null) {
                    psDet.close();
                }
            } catch (Exception ignored) {
            }
            try {
                if (psSol != null) {
                    psSol.close();
                }
            } catch (Exception ignored) {
            }
            try {
                if (cn != null) {
                    cn.setAutoCommit(true);
                }
            } catch (Exception ignored) {
            }
            DbConnection.close(cn);
        }

        return folioGenerado;
    }

    public boolean actualizarEstadoSolicitud(int idSolicitud, String nuevoEstado, int idAdmin, String comentarioAdmin) {
        // Si aprueba, marca fecha_aprobacion = NOW(); si rechaza, también puedes marcarla (o dejar null).
        final String SQL
                = "UPDATE solicitud "
                + "SET estado = ?, "
                + "    comentarios = CONCAT(IFNULL(comentarios,''), "
                + "        CASE WHEN ? IS NULL OR ? = '' THEN '' ELSE CONCAT('\n[ADMIN ', ?, '] ', ?) END), "
                + "    fecha_aprobacion = CASE WHEN ? = 'APROBADA' OR ? = 'RECHAZADA' THEN NOW() ELSE fecha_aprobacion END "
                + "WHERE id_solicitud = ?";

        java.sql.Connection cn = null;
        java.sql.PreparedStatement ps = null;
        try {
            cn = pck_connection.DbConnection.getConnection();
            if (cn == null) {
                return false;
            }

            ps = cn.prepareStatement(SQL);
            ps.setString(1, nuevoEstado);
            ps.setString(2, comentarioAdmin);
            ps.setString(3, comentarioAdmin);
            ps.setInt(4, idAdmin);
            ps.setString(5, comentarioAdmin);
            ps.setString(6, nuevoEstado);
            ps.setString(7, nuevoEstado);
            ps.setInt(8, idSolicitud);

            return ps.executeUpdate() == 1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception ignored) {
            }
            pck_connection.DbConnection.close(cn);
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }

    /**
     * Genera un folio único. Puedes cambiarlo por un SP o lógica propia. Aquí
     * solo usa timestamp + un pequeño sufijo incremental para minimizar
     * colisiones.
     */
    private String generarFolioSolicitud(Connection cn) {
        // Simple y suficiente: SOLyyyyMMddHHmmssSSS
        java.time.format.DateTimeFormatter f = java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String base = java.time.LocalDateTime.now().format(f);
        return "SOL" + base;
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
