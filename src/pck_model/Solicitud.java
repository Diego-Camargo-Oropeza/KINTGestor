package pck_model;

public class Solicitud {

    private int idSolicitud;
    private int idUsuario;
    private String folio;
    private EstadoSolicitud estado;
    private Prioridad prioridad;
    private String comentarios;

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getFolio() {
        return folio;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

}
