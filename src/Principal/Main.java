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
            
            // Ejemplo 2: Agregar un libro con validación de API
            System.out.println("2. Agregando un libro (ISBN válido)...");
            Libro libro = new Libro("9780747532699", "Harry Potter y la Piedra Filosofal", "J.K. Rowling", "disponible");
            bibliotecaService.agregarLibro(libro);
            System.out.println();
            
            // Ejemplo 3: Intentar agregar un libro con ISBN inválido
            System.out.println("3. Intentando agregar un libro con ISBN inválido...");
            try {
                Libro libroInvalido = new Libro("000-0-00-000000-0", "Libro Falso", "Autor Falso", "disponible");
                bibliotecaService.agregarLibro(libroInvalido);
            } catch (Exception e) {
                System.out.println("Esperado: " + e.getMessage());
            }
            System.out.println();
            
            // Ejemplo 4: Listar todos los libros
            System.out.println("4. Listando todos los libros...");
            bibliotecaService.listarLibros();
            System.out.println();
            
            // Ejemplo 5: Listar todos los usuarios
            System.out.println("5. Listando todos los usuarios...");
            usuarioService.listarUsuarios();
            System.out.println();
            
            // Ejemplo 6: Crear un préstamo
            System.out.println("6. Creando un préstamo...");
            Prestamo prestamo = new Prestamo(1, "9780747532699", false);
            prestamoService.crearPrestamo(prestamo);
            System.out.println();
            
            // Ejemplo 7: Listar préstamos
            System.out.println("7. Listando todos los préstamos...");
            prestamoService.listarPrestamos();
            System.out.println();
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== Fin del programa ===");
    }
}

