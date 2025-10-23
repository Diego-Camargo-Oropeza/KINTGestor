package pck_dao;

import pck_connection.DbConnection;
import pck_model.PrestamoRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PrestamoDAO {

    private static final String SQL_ALL
            = "SELECT s.id_solicitud, s.folio, p.nombre AS producto, d.cantidad, s.estado, s.prioridad, s.fecha_creacion "
            + "FROM solicitud s "
            + "JOIN detalle d ON d.id_solicitud = s.id_solicitud "
            + "JOIN producto p ON p.id_producto = d.id_producto "
            + "ORDER BY s.fecha_creacion DESC, s.id_solicitud DESC";

    private static final String SQL_BY_USER
            = "SELECT s.id_solicitud, s.folio, p.nombre AS producto, d.cantidad, s.estado, s.prioridad, s.fecha_creacion "
            + "FROM solicitud s "
            + "JOIN detalle d ON d.id_solicitud = s.id_solicitud "
            + "JOIN producto p ON p.id_producto = d.id_producto "
            + "WHERE s.id_usuario = ? "
            + "ORDER BY s.fecha_creacion DESC, s.id_solicitud DESC";

    public List<PrestamoRow> listAll() {
        List<PrestamoRow> list = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DbConnection.getConnection();
            if (cn == null) {
                return list;
            }
            ps = cn.prepareStatement(SQL_ALL);
            rs = ps.executeQuery();
            while (rs.next()) {
                PrestamoRow r = mapRow(rs);
                list.add(r);
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
        return list;
    }

    public List<PrestamoRow> listByUsuario(int idUsuario) {
        List<PrestamoRow> list = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DbConnection.getConnection();
            if (cn == null) {
                return list;
            }
            ps = cn.prepareStatement(SQL_BY_USER);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            while (rs.next()) {
                PrestamoRow r = mapRow(rs);
                list.add(r);
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
        return list;
    }

    private PrestamoRow mapRow(ResultSet rs) {
        PrestamoRow r = new PrestamoRow();
        try {
            r.setIdSolicitud(rs.getInt("id_solicitud"));
            r.setFolio(rs.getString("folio"));
            r.setProducto(rs.getString("producto"));
            r.setCantidad(rs.getInt("cantidad"));
            r.setEstado(rs.getString("estado"));
            r.setPrioridad(rs.getString("prioridad"));
            r.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener filas de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return r;
    }
}
