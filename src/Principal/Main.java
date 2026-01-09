package Principal;

import modelo.Usuario;
import modelo.Libro;
import modelo.Prestamo;
import servicio.UsuarioService;
import servicio.BibliotecaService;
import servicio.PrestamoService;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Sistema de Biblioteca con Supabase ===\n");
        
        UsuarioService usuarioService = new UsuarioService();
        BibliotecaService bibliotecaService = new BibliotecaService();
        PrestamoService prestamoService = new PrestamoService();
        
        try {
            // Ejemplo 1: Registrar un usuario
            System.out.println("1. Registrando un usuario...");
            Usuario usuario = new Usuario("usuario@ejemplo.com", "password123", "usuario");
            usuarioService.registrarUsuario(usuario);
            System.out.println();
            
            // Ejemplo 2: Agregar un libro
            System.out.println("2. Agregando un libro...");
            Libro libro = new Libro("978-3-16-148410-0", "El Quijote", "Miguel de Cervantes", "disponible");
            bibliotecaService.agregarLibro(libro);
            System.out.println();
            
            // Ejemplo 3: Listar todos los libros
            System.out.println("3. Listando todos los libros...");
            bibliotecaService.listarLibros();
            System.out.println();
            
            // Ejemplo 4: Listar todos los usuarios
            System.out.println("4. Listando todos los usuarios...");
            usuarioService.listarUsuarios();
            System.out.println();
            
            // Ejemplo 5: Crear un préstamo
            System.out.println("5. Creando un préstamo...");
            Prestamo prestamo = new Prestamo(1, "978-3-16-148410-0", false);
            prestamoService.crearPrestamo(prestamo);
            System.out.println();
            
            // Ejemplo 6: Listar préstamos
            System.out.println("6. Listando todos los préstamos...");
            prestamoService.listarPrestamos();
            System.out.println();
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== Fin del programa ===");
    }
}

