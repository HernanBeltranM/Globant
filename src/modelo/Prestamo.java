package modelo;

public class Prestamo {
    private Long id_prestamo;
    private Integer usuario_id;
    private String libro_isbn;
    private String fecha_adquisicion;
    private String fecha_solicitud;
    private String fecha_aprobacion;
    private String fecha_expiracion;
    private Boolean comprado;
    
    public Prestamo() {
    }
    
    public Prestamo(Integer usuario_id, String libro_isbn, Boolean comprado) {
        this.usuario_id = usuario_id;
        this.libro_isbn = libro_isbn;
        this.comprado = comprado;
    }
    
    public Long getId_prestamo() {
        return id_prestamo;
    }
    
    public void setId_prestamo(Long id_prestamo) {
        this.id_prestamo = id_prestamo;
    }
    
    public Integer getUsuario_id() {
        return usuario_id;
    }
    
    public void setUsuario_id(Integer usuario_id) {
        this.usuario_id = usuario_id;
    }
    
    public String getLibro_isbn() {
        return libro_isbn;
    }
    
    public void setLibro_isbn(String libro_isbn) {
        this.libro_isbn = libro_isbn;
    }
    
    public String getFecha_adquisicion() {
        return fecha_adquisicion;
    }
    
    public void setFecha_adquisicion(String fecha_adquisicion) {
        this.fecha_adquisicion = fecha_adquisicion;
    }
    
    public String getFecha_solicitud() {
        return fecha_solicitud;
    }
    
    public void setFecha_solicitud(String fecha_solicitud) {
        this.fecha_solicitud = fecha_solicitud;
    }
    
    public String getFecha_aprobacion() {
        return fecha_aprobacion;
    }
    
    public void setFecha_aprobacion(String fecha_aprobacion) {
        this.fecha_aprobacion = fecha_aprobacion;
    }
    
    public String getFecha_expiracion() {
        return fecha_expiracion;
    }
    
    public void setFecha_expiracion(String fecha_expiracion) {
        this.fecha_expiracion = fecha_expiracion;
    }
    
    public Boolean getComprado() {
        return comprado;
    }
    
    public void setComprado(Boolean comprado) {
        this.comprado = comprado;
    }
    
    @Override
    public String toString() {
        return "Prestamo{" +
                "id_prestamo=" + id_prestamo +
                ", usuario_id=" + usuario_id +
                ", libro_isbn='" + libro_isbn + '\'' +
                ", comprado=" + comprado +
                '}';
    }
}
