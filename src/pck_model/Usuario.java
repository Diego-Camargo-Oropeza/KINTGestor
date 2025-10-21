package pck_model;

public class Usuario {

    private int idUsuario;
    private int idRol;
    private String nombre;
    private String correo;
    private String contrasenaHash;
    private boolean activo;
    private String rolNombre;

    public Usuario(int idUsuario, int idRol, String nombre, String correo, String contrasenaHash, boolean activo, String rolNombre) {
        this.idUsuario = idUsuario;
        this.idRol = idRol;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenaHash = contrasenaHash;
        this.activo = activo;
        this.rolNombre = rolNombre;
    }

    public Usuario() {
        this(0, 3, null, null, null, false, null);
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdRol() {
        return idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public boolean isActivo() {
        return activo;
    }

    public String getRolNombre() {
        return rolNombre;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }

}
