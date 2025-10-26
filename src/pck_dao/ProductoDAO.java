package pck_dao;

import pck_connection.DbConnection;
import pck_model.ProductoRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pck_model.Producto;
import pck_model.TipoProducto;

public class ProductoDAO {

    // ---------- Select base ----------
    private static final String BASE_SELECT_JOIN
            = "SELECT p.id_producto, p.sku, p.nombre, p.descripcion, p.tipo, p.marca, "
            + "       p.id_categoria, COALESCE(c.nombre,'') AS categoria_nombre, "
            + "       p.u_medida, p.codigo_barras, p.stock, p.ubicacion, p.activo, "
            + "       p.fecha_creacion, p.fecha_edicion "
            + "FROM producto p "
            + "LEFT JOIN categoria c ON c.id_categoria = p.id_categoria ";

    // ---------- Mappers ----------
    private Producto mapProducto(ResultSet rs) {
        Producto p = new Producto();
        try {
            p.setIdProducto(rs.getInt("id_producto"));
            p.setSku(rs.getString("sku"));
            p.setNombre(rs.getString("nombre"));
            p.setDescripcion(rs.getString("descripcion"));

            String tipoStr = rs.getString("tipo");
            p.setTipo(tipoStr == null ? null : TipoProducto.valueOf(tipoStr));

            p.setMarca(rs.getString("marca"));
            p.setIdCategoria(rs.getInt("id_categoria"));

            try {
                p.setCategoriaNombre(rs.getString("categoria_nombre"));
            } catch (SQLException ignore) {
            }
            p.setuMedida(rs.getString("u_medida"));
            p.setCodigoBarras(rs.getString("codigo_barras"));
            p.setStock(rs.getInt("stock"));
            p.setUbicacion(rs.getString("ubicacion"));
            p.setActivo(rs.getBoolean("activo"));
            p.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
            p.setFechaEdicion(rs.getTimestamp("fecha_edicion"));
        } catch (SQLException ignore) {
        }
        return p;
    }

    private ProductoRow mapRow(ResultSet rs) {
        ProductoRow r = new ProductoRow();
        try {
            r.setIdProducto(rs.getInt("id_producto"));
            r.setSku(rs.getString("sku"));
            r.setNombre(rs.getString("nombre"));
            r.setTipo(rs.getString("tipo"));
            r.setCategoria(rs.getString("categoria_nombre"));
            r.setUMedida(rs.getString("u_medida"));
            r.setStock(rs.getInt("stock"));
            r.setUbicacion(rs.getString("ubicacion"));
            r.setActivo(rs.getBoolean("activo"));
            r.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        } catch (SQLException ignore) {
        }
        return r;
    }

    // ---------- Listados para la tabla ----------
    public List<ProductoRow> listAll() {
        String sql = BASE_SELECT_JOIN + "ORDER BY p.nombre";
        List<ProductoRow> list = new ArrayList<>();
        try {
            Connection cn = DbConnection.getConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Básicas utiles para vistas que no requieren descripción/marca/etc.
    public List<ProductoRow> listAllBasic() {
        String sql
                = "SELECT p.id_producto, p.sku, p.nombre, p.tipo, "
                + "       COALESCE(c.nombre,'') AS categoria_nombre, "
                + "       p.u_medida, p.stock, p.ubicacion, p.activo, p.fecha_creacion "
                + "FROM producto p "
                + "LEFT JOIN categoria c ON c.id_categoria = p.id_categoria "
                + "ORDER BY p.nombre";
        List<ProductoRow> list = new ArrayList<>();
        try {
            Connection cn = DbConnection.getConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ProductoRow> listByTipoBasic(String tipo) {
        String sql
                = "SELECT p.id_producto, p.sku, p.nombre, p.tipo, "
                + "       COALESCE(c.nombre,'') AS categoria_nombre, "
                + "       p.u_medida, p.stock, p.ubicacion, p.activo, p.fecha_creacion "
                + "FROM producto p "
                + "LEFT JOIN categoria c ON c.id_categoria = p.id_categoria "
                + "WHERE p.tipo = ? "
                + "ORDER BY p.nombre";
        List<ProductoRow> list = new ArrayList<>();
        try {
            Connection cn = DbConnection.getConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ProductoRow> listByCategoriaId(int idCategoria) {
        String sql = BASE_SELECT_JOIN + "WHERE p.id_categoria=? ORDER BY p.nombre";
        List<ProductoRow> list = new ArrayList<>();
        try {
            Connection cn = DbConnection.getConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ProductoRow> listByTipo(String tipo) {
        String sql = BASE_SELECT_JOIN + "WHERE p.tipo=? ORDER BY p.nombre";
        List<ProductoRow> list = new ArrayList<>();
        try {
            Connection cn = DbConnection.getConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ProductoRow> listByTipoAndCategoria(String tipo, int idCategoria) {
        String sql = BASE_SELECT_JOIN + "WHERE p.tipo=? AND p.id_categoria=? ORDER BY p.nombre";
        List<ProductoRow> list = new ArrayList<>();
        try {
            Connection cn = DbConnection.getConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, tipo);
            ps.setInt(2, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ---------- CRUD de Producto (modelo completo) ----------
    public Producto findById(int idProducto) {
        String sql = BASE_SELECT_JOIN + "WHERE p.id_producto=?";
        try (Connection cn = DbConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapProducto(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int insert(Producto p) {
        String sql = "INSERT INTO producto "
                + "(sku, nombre, descripcion, tipo, marca, id_categoria, u_medida, "
                + " codigo_barras, stock, ubicacion, activo, fecha_creacion) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,NOW())";
        try {
            Connection cn = DbConnection.getConnection();
            PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getSku());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setString(4, p.getTipo() == null ? null : p.getTipo().name());
            ps.setString(5, p.getMarca());
            ps.setInt(6, p.getIdCategoria());
            ps.setString(7, p.getuMedida());
            ps.setString(8, p.getCodigoBarras());
            ps.setInt(9, p.getStock());
            ps.setString(10, p.getUbicacion());
            ps.setBoolean(11, p.isActivo());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean update(Producto p) {
        String sql = "UPDATE producto SET "
                + "sku=?, nombre=?, descripcion=?, tipo=?, marca=?, id_categoria=?, "
                + "u_medida=?, codigo_barras=?, stock=?, ubicacion=?, activo=?, fecha_edicion=NOW() "
                + "WHERE id_producto=?";
        try {
            Connection cn = DbConnection.getConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, p.getSku());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setString(4, p.getTipo() == null ? null : p.getTipo().name());
            ps.setString(5, p.getMarca());
            ps.setInt(6, p.getIdCategoria());
            ps.setString(7, p.getuMedida());
            ps.setString(8, p.getCodigoBarras());
            ps.setInt(9, p.getStock());
            ps.setString(10, p.getUbicacion());
            ps.setBoolean(11, p.isActivo());
            ps.setInt(12, p.getIdProducto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean disable(int idProducto) {
        String sql = "UPDATE producto SET activo=0, fecha_edicion=NOW() WHERE id_producto=?";
        try (Connection cn = DbConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteHard(int idProducto) {
        String sql = "DELETE FROM producto WHERE id_producto=?";
        try (Connection cn = DbConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------- Totales para resumen ----------
    public int countAll() {
        return countSimple("SELECT COUNT(*) FROM producto");
    }

    public int countAgotados() {
        return countSimple("SELECT COUNT(*) FROM producto WHERE stock<=0");
    }

    public int countBajoStock(int umbral) {
        String sql = "SELECT COUNT(*) FROM producto WHERE stock>0 AND stock<=?";
        try {
            Connection cn = DbConnection.getConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, Math.max(0, umbral));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int countSimple(String sql) {
        try {
            Connection cn = DbConnection.getConnection();
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
