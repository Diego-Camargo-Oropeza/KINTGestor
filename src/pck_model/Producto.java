package pck_model;

import java.util.Date;
import java.util.Objects;
import static pck_model.TipoProducto.MATERIAL;

public class Producto {

    private int idProducto;

    // --- Atributos del producto
    private String sku;
    private String nombre;
    private String descripcion;
    private TipoProducto tipo;              // enum: MATERIAL/HERRAMIENTA/DISPOSITIVO
    private String marca;

    // --- Categoría 
    private int idCategoria;                // FK a kint_inv_categoria.id_categoria
    private String categoriaNombre;         // Solo lectura/convivencia para listas

    // --- Inventario
    private String uMedida;
    private String codigoBarras;
    private int stock;
    private String ubicacion;

    // --- Estado y auditoría
    private boolean activo;
    private Date fechaCreacion;
    private Date fechaEdicion;

    public Producto() {
        this(0, null, null, null, MATERIAL, null, 0, null, null, null, 0, null, false, null, null);
    }

    public Producto(String sku, String nombre, TipoProducto tipo, int idCategoria) {
        this.sku = sku;
        this.nombre = nombre;
        this.tipo = tipo;
        this.idCategoria = idCategoria;
        this.activo = true;
        this.stock = 0;
    }

    public Producto(int idProducto, String sku, String nombre, String descripcion, TipoProducto tipo,
            String marca, int idCategoria, String categoriaNombre, String uMedida,
            String codigoBarras, int stock, String ubicacion, boolean activo,
            Date fechaCreacion, Date fechaEdicion) {
        this.idProducto = idProducto;
        this.sku = sku;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.marca = marca;
        this.idCategoria = idCategoria;
        this.categoriaNombre = categoriaNombre;
        this.uMedida = uMedida;
        this.codigoBarras = codigoBarras;
        setStock(stock);
        this.ubicacion = ubicacion;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
        this.fechaEdicion = fechaEdicion;
    }

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

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public String getuMedida() {
        return uMedida;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public int getStock() {
        return stock;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public Date getFechaEdicion() {
        return fechaEdicion;
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

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public void setuMedida(String uMedida) {
        this.uMedida = uMedida;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setFechaEdicion(Date fechaEdicion) {
        this.fechaEdicion = fechaEdicion;
    }

    public boolean isAgotado() {
        return stock <= 0;
    }

    public boolean isBajoStock(int umbral) {
        return stock > 0 && stock <= Math.max(0, umbral);
    }

    public boolean isTipo(TipoProducto tipo) {
        return this.tipo == tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Producto)) {
            return false;
        }
        Producto that = (Producto) o;
        // Si ya tiene ID en BD, comparamos por ID; si no, por SKU (asumiendo único)
        if (this.idProducto > 0 && that.idProducto > 0) {
            return this.idProducto == that.idProducto;
        }
        return Objects.equals(this.sku, that.sku);
    }

    @Override
    public int hashCode() {
        return (idProducto > 0) ? Integer.hashCode(idProducto) : Objects.hashCode(sku);
    }

    @Override
    public String toString() {
        // Útil si lo usas directo en combos/tablas
        return nombre != null ? nombre : ("Producto #" + idProducto);
    }

}
