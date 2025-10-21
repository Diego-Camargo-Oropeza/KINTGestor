package pck_model;

public class Producto {

    private int idProducto;
    private String sku;
    private String nombre;
    private String descripcion;
    private TipoProducto tipo;  // enum
    private String marca;
    private String categoria;
    private String uMedida;
    private String codigoBarras;
    private boolean activo;
    
    public int getIdProducto() {
        return idProducto;
    }

    public String getSku() {
        return sku;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public TipoProducto getTipo() {
        return tipo;
    }

    public String getMarca() {
        return marca;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getuMedida() {
        return uMedida;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTipo(TipoProducto tipo) {
        this.tipo = tipo;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setuMedida(String uMedida) {
        this.uMedida = uMedida;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    

    
    
}
