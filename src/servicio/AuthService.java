package servicio;

import modelo.Usuario;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AuthService {
    
    /**
     * Authenticate a user by email and password
     * @param correo User's email
     * @param contrasena User's plain text password
     * @return Usuario object if authentication is successful, null otherwise
     */
    public Usuario autenticar(String correo, String contrasena) {
        try {
            // URL encode the email to prevent issues with special characters
            String encodedEmail = URLEncoder.encode(correo, StandardCharsets.UTF_8.toString());
            
            // Search for user by email
            String url = SupabaseConfig.USUARIOS_URL + "?correo=eq." + encodedEmail;
            String response = SupabaseClient.get(url);
            
            // Check if response is empty array
            if (response.trim().equals("[]")) {
                System.out.println("Usuario no encontrado.");
                return null;
            }
            
            // Parse JSON manually (simple extraction)
            String storedPassword = extractJsonValue(response, "contrasena");
            
            if (storedPassword == null) {
                System.out.println("Error al leer datos del usuario.");
                return null;
            }
            
            // Verify password
            if (PasswordHasher.verifyPassword(contrasena, storedPassword)) {
                // Create Usuario object
                Usuario usuario = new Usuario();
                
                String idStr = extractJsonValue(response, "id");
                if (idStr != null) {
                    usuario.setId(Integer.parseInt(idStr));
                }
                
                usuario.setCorreo(extractJsonValue(response, "correo"));
                usuario.setContrasena(storedPassword);
                usuario.setRol(extractJsonValue(response, "rol"));
                
                String oath = extractJsonValue(response, "oath");
                if (oath != null && !oath.equals("null")) {
                    usuario.setOath(oath);
                }
                
                return usuario;
            } else {
                System.out.println("Contraseña incorrecta.");
                return null;
            }
            
        } catch (Exception e) {
            System.err.println("Error durante la autenticación: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Extract a value from a JSON string
     * @param json The JSON string
     * @param key The key to extract
     * @return The extracted value or null
     */
    private String extractJsonValue(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int startIndex = json.indexOf(searchKey);
            if (startIndex == -1) {
                return null;
            }
            
            startIndex += searchKey.length();
            
            // Skip whitespace
            while (startIndex < json.length() && Character.isWhitespace(json.charAt(startIndex))) {
                startIndex++;
            }
            
            if (startIndex >= json.length()) {
                return null;
            }
            
            // Check if value is a string (starts with ")
            if (json.charAt(startIndex) == '"') {
                startIndex++; // Skip opening quote
                int endIndex = json.indexOf("\"", startIndex);
                if (endIndex == -1) {
                    return null;
                }
                return json.substring(startIndex, endIndex);
            } else if (json.charAt(startIndex) == 'n' && json.startsWith("null", startIndex)) {
                return null;
            } else {
                // It's a number or boolean
                int endIndex = startIndex;
                while (endIndex < json.length() && 
                       json.charAt(endIndex) != ',' && 
                       json.charAt(endIndex) != '}' && 
                       json.charAt(endIndex) != ']') {
                    endIndex++;
                }
                return json.substring(startIndex, endIndex).trim();
            }
        } catch (Exception e) {
            return null;
        }
    }
}
