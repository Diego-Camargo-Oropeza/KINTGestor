package pck_dao;

import pck_connection.DbConnection;
import pck_model.ProductoRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<ProductoRow> listAll() {
        String sql = "SELECT id_producto, sku, nombre, tipo, categoria, u_medida, activo, fecha_creacion "
                + "FROM producto ORDER BY nombre";
        List<ProductoRow> list = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DbConnection.getConnection();
            ps = cn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                ProductoRow r = new ProductoRow();
                r.setIdProducto(rs.getInt("id_producto"));
                r.setSku(rs.getString("sku"));
                r.setNombre(rs.getString("nombre"));
                r.setTipo(rs.getString("tipo"));
                r.setCategoria(rs.getString("categoria"));
                r.setUMedida(rs.getString("u_medida"));
                r.setActivo(rs.getBoolean("activo"));
                r.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                list.add(r);
            }
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
        return list;
    }

    public List<ProductoRow> listByTipo(String tipo) {
        String sql = "SELECT id_producto, sku, nombre, tipo, categoria, u_medida, activo, fecha_creacion "
                + "FROM producto WHERE tipo=? ORDER BY nombre";
        List<ProductoRow> list = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DbConnection.getConnection();
            ps = cn.prepareStatement(sql);
            ps.setString(1, tipo);
            rs = ps.executeQuery();
            while (rs.next()) {
                ProductoRow r = new ProductoRow();
                r.setIdProducto(rs.getInt("id_producto"));
                r.setSku(rs.getString("sku"));
                r.setNombre(rs.getString("nombre"));
                r.setTipo(rs.getString("tipo"));
                r.setCategoria(rs.getString("categoria"));
                r.setUMedida(rs.getString("u_medida"));
                r.setActivo(rs.getBoolean("activo"));
                r.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                list.add(r);
            }
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
        return list;
    }
    
    public int countAll() {
        return countSimple("SELECT COUNT(*) FROM producto");
    }

    public int countActivos() {
        return countSimple("SELECT COUNT(*) FROM producto WHERE activo=1");
    }

    public int countByTipo(String tipo) {
        String sql = "SELECT COUNT(*) FROM producto WHERE tipo=?";
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DbConnection.getConnection();
            ps = cn.prepareStatement(sql);
            ps.setString(1, tipo);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
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
        return 0;
    }

    private int countSimple(String sql) {
        Connection cn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            cn = DbConnection.getConnection();
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException ignored) {
            }
            if (st != null) try {
                st.close();
            } catch (SQLException ignored) {
            }
            DbConnection.close(cn);
        }
        return 0;
    }
}
