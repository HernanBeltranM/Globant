package servicio;

import modelo.Usuario;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthService {
    
    private UsuarioService usuarioService;
    
    public AuthService() {
        this.usuarioService = new UsuarioService();
    }
    
    /**
     * Authenticate a user with email and password
     * @return Usuario object if authentication is successful, null otherwise
     */
    public Usuario login(String correo, String contrasena) throws Exception {
        // Search for user by email
        String encodedEmail = URLEncoder.encode(correo, StandardCharsets.UTF_8.toString());
        String url = SupabaseConfig.USUARIOS_URL + "?correo=eq." + encodedEmail;
        String response = SupabaseClient.get(url);
        
        // Check if response is empty array
        if (response == null || response.trim().equals("[]")) {
            System.out.println("Usuario no encontrado.");
            return null;
        }
        
        // Simple JSON parsing for user data
        String storedHash = extractJsonValue(response, "contrasena");
        
        if (storedHash == null) {
            System.out.println("Error al obtener datos del usuario.");
            return null;
        }
        
        // Verify password
        if (PasswordHasher.verifyPassword(contrasena, storedHash)) {
            // Create Usuario object
            Usuario usuario = new Usuario();
            usuario.setId(Integer.parseInt(extractJsonValue(response, "id")));
            usuario.setCorreo(extractJsonValue(response, "correo"));
            usuario.setContrasena(storedHash);
            usuario.setRol(extractJsonValue(response, "rol"));
            
            String oath = extractJsonValue(response, "oath");
            if (oath != null && !oath.equals("null")) {
                usuario.setOath(oath);
            }
            
            System.out.println("Login exitoso: " + usuario.getCorreo() + " (Rol: " + usuario.getRol() + ")");
            return usuario;
        } else {
            System.out.println("Contraseña incorrecta.");
            return null;
        }
    }
    
    /**
     * Simple JSON value extractor using regex.
     * Note: This is a simple implementation for basic JSON responses.
     * For complex JSON structures, consider using a proper JSON library like Jackson or Gson.
     */
    private String extractJsonValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"?([^\",}]*)\"?");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    
    /**
     * Check if a user can create another user based on roles
     */
    public boolean canCreateUser(String creatorRole, String newUserRole) {
        if (creatorRole == null || newUserRole == null) {
            return false;
        }
        
        switch (creatorRole.toLowerCase()) {
            case "super_admin":
                // Superadmin can create anyone
                return true;
            case "admin":
                // Admin can only create normal users
                return newUserRole.toLowerCase().equals("normal");
            case "normal":
                // Normal users cannot create other users
                return false;
            default:
                return false;
        }
    }
    
    /**
     * Create a new user with role-based permission check
     */
    public void crearUsuario(Usuario creador, String nuevoCorreo, String nuevaContrasena, String nuevoRol) throws Exception {
        // Check if creator has permission
        if (!canCreateUser(creador.getRol(), nuevoRol)) {
            throw new Exception("No tienes permisos para crear un usuario con rol: " + nuevoRol);
        }
        
        // Hash password
        String hashedPassword = PasswordHasher.hashPassword(nuevaContrasena);
        
        // Create new user
        Usuario nuevoUsuario = new Usuario(nuevoCorreo, hashedPassword, nuevoRol);
        usuarioService.registrarUsuario(nuevoUsuario);
        
        System.out.println("Usuario creado exitosamente: " + nuevoCorreo + " (Rol: " + nuevoRol + ")");
    }
}
