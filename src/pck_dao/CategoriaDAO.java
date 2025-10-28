package pck_dao;

import pck_model.Categoria;
import pck_connection.DbConnection;
import pck_model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    // ============ SQL ============
    private static final String SQL_SELECT_BASE
            = "SELECT id_categoria, nombre, descripcion FROM categoria";

    private static final String SQL_SELECT_ALL
            = SQL_SELECT_BASE + " ORDER BY nombre";

    private static final String SQL_SELECT_BY_ID
            = SQL_SELECT_BASE + " WHERE id_categoria=?";

    private static final String SQL_INSERT
            = "INSERT INTO categoria (nombre, descripcion) VALUES (?, ?)";

    private static final String SQL_UPDATE
            = "UPDATE categoria SET nombre=?, descripcion=? WHERE id_categoria=?";

    private static final String SQL_DELETE_HARD
            = "DELETE FROM categoria WHERE id_categoria=?";

    // ============ MAPPER ============
    private Categoria map(ResultSet rs) {
        Categoria c = new Categoria();
        try {
            c.setIdCategoria(rs.getInt("id_categoria"));
            c.setNombre(rs.getString("nombre"));
            c.setDescripcion(rs.getString("descripcion"));
        } catch (SQLException e) {

        }

        return c;
    }

    // ============ QUERIES ============
    public List<Categoria> findAll() {
        List<Categoria> list = new ArrayList<>();
        try {
            Connection cn = DbConnection.getConnection();
            PreparedStatement ps = cn.prepareStatement(SQL_SELECT_ALL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Categoria findById(int idCategoria) {
        try {
            Connection cn = DbConnection.getConnection();
            PreparedStatement ps = cn.prepareStatement(SQL_SELECT_BY_ID);
            ps.setInt(1, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ============ CRUD ============
    public int insert(Categoria c) {
        try {
            Connection cn = DbConnection.getConnection();
            PreparedStatement ps = cn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());
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
        return 0; // no se generó ID
    }

    public boolean update(Categoria c) {
        try {
            Connection cn = DbConnection.getConnection();
            PreparedStatement ps = cn.prepareStatement(SQL_UPDATE);
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());
            ps.setInt(3, c.getIdCategoria());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteHard(int idCategoria) {
        try {
            Connection cn = DbConnection.getConnection();
            PreparedStatement ps = cn.prepareStatement(SQL_DELETE_HARD);
            ps.setInt(1, idCategoria);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
