package Principal;

import servicio.UsuarioService;

public class CrearSuperAdmin {
    public static void main(String[] args) {
        System.out.println("=== Creando Usuario SuperAdmin ===\n");
        
        UsuarioService usuarioService = new UsuarioService();
        
        try {
            // Create the superadmin user with hashed password
            // Email: beltranmunozh@gmail.com
            // Password: Hernan2007*
            // Role: superadmin
            
            usuarioService.registrarUsuarioConHash(
                "beltranmunozh@gmail.comm",
                "Hernan2007*",
                "super_admin"
            );
            
            System.out.println("\n¡SuperAdmin creado exitosamente!");
            System.out.println("Correo: beltranmunozh@gmail.com");
            System.out.println("Contraseña: Hernan2007*");
            System.out.println("Rol: superadmin");
            
        } catch (Exception e) {
            System.err.println("Error al crear superadmin: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
