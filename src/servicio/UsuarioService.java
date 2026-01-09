package servicio;

import modelo.Usuario;

public class UsuarioService {
    
    public void registrarUsuario(Usuario usuario) throws Exception {
        String json = String.format(
            "{\"correo\":\"%s\",\"contrasena\":\"%s\",\"rol\":\"%s\"%s}",
            usuario.getCorreo(), 
            usuario.getContrasena(), 
            usuario.getRol(),
            usuario.getOath() != null ? ",\"oath\":\"" + usuario.getOath() + "\"" : ""
        );
        String response = SupabaseClient.post(SupabaseConfig.USUARIOS_URL, json);
        System.out.println("Usuario registrado: " + response);
    }
    
    public void registrarUsuarioConHash(String correo, String contrasena, String rol) throws Exception {
        // Hash password before storing
        String hashedPassword = PasswordHasher.hashPassword(contrasena);
        Usuario usuario = new Usuario(correo, hashedPassword, rol);
        registrarUsuario(usuario);
    }
    
    public void listarUsuarios() throws Exception {
        String response = SupabaseClient.get(SupabaseConfig.USUARIOS_URL);
        System.out.println("Usuarios: " + response);
    }
    
    public void buscarUsuarioPorId(Integer id) throws Exception {
        String url = SupabaseConfig.USUARIOS_URL + "?id=eq." + id;
        String response = SupabaseClient.get(url);
        System.out.println("Usuario encontrado: " + response);
    }
    
    public void buscarUsuarioPorCorreo(String correo) throws Exception {
        String url = SupabaseConfig.USUARIOS_URL + "?correo=eq." + correo;
        String response = SupabaseClient.get(url);
        System.out.println("Usuario encontrado: " + response);
    }
    
    public void actualizarRolUsuario(Integer id, String nuevoRol) throws Exception {
        String url = SupabaseConfig.USUARIOS_URL + "?id=eq." + id;
        String json = String.format("{\"rol\":\"%s\"}", nuevoRol);
        String response = SupabaseClient.patch(url, json);
        System.out.println("Rol del usuario actualizado: " + response);
    }
}
