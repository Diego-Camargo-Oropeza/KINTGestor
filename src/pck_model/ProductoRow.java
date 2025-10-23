package pck_model;

import java.util.Date;

public class ProductoRow {

    private int idProducto;
    private String sku;
    private String nombre;
    private String tipo;       // MATERIAL | HERRAMIENTA | DISPOSITIVO
    private String categoria;
    private String uMedida;
    private boolean activo;
    private Date fechaCreacion;

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int v) {
        idProducto = v;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String v) {
        sku = v;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String v) {
        nombre = v;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String v) {
        tipo = v;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String v) {
        categoria = v;
    }

    public String getUMedida() {
        return uMedida;
    }

    public void setUMedida(String v) {
        uMedida = v;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean v) {
        activo = v;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date v) {
        fechaCreacion = v;
    }
}
