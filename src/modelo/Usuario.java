package modelo;

public class Usuario {
    private Integer id;
    private String correo;
    private String contrasena;
    private String oath;
    private String rol;
    
    public Usuario() {
    }
    
    public Usuario(String correo, String contrasena, String rol) {
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getCorreo() {
        return correo;
    }
    
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public String getContrasena() {
        return contrasena;
    }
    
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    public String getOath() {
        return oath;
    }
    
    public void setOath(String oath) {
        this.oath = oath;
    }
    
    public String getRol() {
        return rol;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", correo='" + correo + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}
