package pck_model;

import java.util.Date;

public class ProductoRow {

    private int idProducto;
    private String sku;
    private String nombre;
    private String tipo;
    private String categoria;     
    private String uMedida;

    private int stock;            
    private String ubicacion;     

    private boolean activo;
    private java.util.Date fechaCreacion;

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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
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

    public void setFechaCreacion(Date date) {
        fechaCreacion = date;
    }
}
