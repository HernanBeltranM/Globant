package Principal;

import modelo.Usuario;
import modelo.Libro;
import modelo.Prestamo;
import servicio.UsuarioService;
import servicio.BibliotecaService;
import servicio.PrestamoService;
import servicio.AuthService;
import servicio.LibrosAPIValidator;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Usuario usuarioActual = null;
    
    public static void main(String[] args) {
        System.out.println("=== Sistema de Biblioteca con Supabase ===\n");
        
        // Login required
        if (!login()) {
            System.out.println("No se pudo autenticar. Saliendo del sistema.");
            scanner.close();
            return;
        }
        
        // Show interactive menu
        mostrarMenuPrincipal();
        
        scanner.close();
        System.out.println("\n=== Fin del programa ===");
    }
    
    /**
     * Login process
     */
    private static boolean login() {
        System.out.println("=== LOGIN ===");
        AuthService authService = new AuthService();
        
        int intentos = 3;
        while (intentos > 0) {
            System.out.print("Correo: ");
            String correo = scanner.nextLine();
            
            System.out.print("Contraseña: ");
            String contrasena = scanner.nextLine();
            
            usuarioActual = authService.autenticar(correo, contrasena);
            
            if (usuarioActual != null) {
                System.out.println("\n✓ Bienvenido, " + usuarioActual.getCorreo() + " (Rol: " + usuarioActual.getRol() + ")");
                return true;
            } else {
                intentos--;
                if (intentos > 0) {
                    System.out.println("Credenciales incorrectas. Intentos restantes: " + intentos + "\n");
                }
            }
        }
        
        return false;
    }
    
    /**
     * Show main menu
     */
    private static void mostrarMenuPrincipal() {
        BibliotecaService bibliotecaService = new BibliotecaService();
        UsuarioService usuarioService = new UsuarioService();
        PrestamoService prestamoService = new PrestamoService();
        
        boolean continuar = true;
        
        while (continuar) {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Buscar información de un libro");
            System.out.println("2. Agregar libro a la biblioteca");
            System.out.println("3. Listar todos los libros");
            System.out.println("4. Crear préstamo");
            System.out.println("5. Listar préstamos");
            System.out.println("6. Registrar nuevo usuario");
            System.out.println("7. Listar usuarios");
            System.out.println("0. Salir");
            System.out.print("\nSeleccione una opción: ");
            
            String opcion = scanner.nextLine();
            
            try {
                switch (opcion) {
                    case "1":
                        buscarInfoLibro();
                        break;
                    case "2":
                        agregarLibro(bibliotecaService);
                        break;
                    case "3":
                        bibliotecaService.listarLibros();
                        break;
                    case "4":
                        crearPrestamo(prestamoService);
                        break;
                    case "5":
                        prestamoService.listarPrestamos();
                        break;
                    case "6":
                        registrarUsuario(usuarioService);
                        break;
                    case "7":
                        usuarioService.listarUsuarios();
                        break;
                    case "0":
                        continuar = false;
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
    
    /**
     * Search for book information
     */
    private static void buscarInfoLibro() {
        System.out.print("\nIngrese el ISBN del libro: ");
        String isbn = scanner.nextLine().trim();
        
        if (isbn.isEmpty()) {
            System.out.println("ISBN no puede estar vacío.");
            return;
        }
        
        System.out.println("\nBuscando información del libro...");
        LibrosAPIValidator.mostrarInfoLibro(isbn);
    }
    
    /**
     * Add a book to the library
     */
    private static void agregarLibro(BibliotecaService bibliotecaService) throws Exception {
        System.out.print("\nIngrese el ISBN del libro: ");
        String isbn = scanner.nextLine().trim();
        
        System.out.print("Ingrese el título: ");
        String titulo = scanner.nextLine().trim();
        
        System.out.print("Ingrese el autor: ");
        String autor = scanner.nextLine().trim();
        
        System.out.print("Ingrese el estado (disponible/no disponible/temporalmente no disponible): ");
        String estado = scanner.nextLine().trim();
        
        if (isbn.isEmpty() || titulo.isEmpty() || autor.isEmpty() || estado.isEmpty()) {
            System.out.println("Todos los campos son requeridos.");
            return;
        }
        
        Libro libro = new Libro(isbn, titulo, autor, estado);
        bibliotecaService.agregarLibro(libro);
    }
    
    /**
     * Create a loan
     */
    private static void crearPrestamo(PrestamoService prestamoService) throws Exception {
        System.out.print("\nIngrese el ID del usuario: ");
        String userIdStr = scanner.nextLine().trim();
        
        System.out.print("Ingrese el ISBN del libro: ");
        String isbn = scanner.nextLine().trim();
        
        System.out.print("¿Es una compra? (s/n): ");
        String esCompraStr = scanner.nextLine().trim().toLowerCase();
        
        if (userIdStr.isEmpty() || isbn.isEmpty()) {
            System.out.println("Todos los campos son requeridos.");
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdStr);
            boolean esCompra = esCompraStr.equals("s") || esCompraStr.equals("si");
            
            Prestamo prestamo = new Prestamo(userId, isbn, esCompra);
            prestamoService.crearPrestamo(prestamo);
        } catch (NumberFormatException e) {
            System.out.println("ID de usuario debe ser un número.");
        }
    }
    
    /**
     * Register a new user
     */
    private static void registrarUsuario(UsuarioService usuarioService) throws Exception {
        System.out.print("\nIngrese el correo: ");
        String correo = scanner.nextLine().trim();
        
        System.out.print("Ingrese la contraseña: ");
        String contrasena = scanner.nextLine().trim();
        
        System.out.print("Ingrese el rol (usuario/admin/super_admin): ");
        String rol = scanner.nextLine().trim();
        
        if (correo.isEmpty() || contrasena.isEmpty() || rol.isEmpty()) {
            System.out.println("Todos los campos son requeridos.");
            return;
        }
        
        Usuario usuario = new Usuario(correo, contrasena, rol);
        usuarioService.registrarUsuario(usuario);
    }
}

