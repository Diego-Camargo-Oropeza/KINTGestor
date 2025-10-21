package pck_model;
public class Detalle {
    private int idDetalle;
    private int idSolicitud;
    private int idProducto;
    private int cantidad;
    private String observaciones;

    public int getIdDetalle() {
        return idDetalle;
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    
}
