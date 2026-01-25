package Principal;

import modelo.Usuario;
import modelo.Libro;
import modelo.Prestamo;
import servicio.UsuarioService;
import servicio.BibliotecaService;
import servicio.PrestamoService;
import servicio.AuthService;

import java.util.Scanner;

public class Main {
    
    private static Scanner scanner = new Scanner(System.in);
    private static AuthService authService = new AuthService();
    private static UsuarioService usuarioService = new UsuarioService();
    private static BibliotecaService bibliotecaService = new BibliotecaService();
    private static PrestamoService prestamoService = new PrestamoService();
    private static Usuario usuarioActual = null;
    
    public static void main(String[] args) {
        System.out.println("=== Sistema de Biblioteca con Supabase ===\n");
        
        // Main loop
        while (true) {
            if (usuarioActual == null) {
                mostrarMenuLogin();
            } else {
                mostrarMenuPrincipal();
            }
        }
    }
    
    private static void mostrarMenuLogin() {
        System.out.println("\n=== MENÚ DE LOGIN ===");
        System.out.println("1. Iniciar Sesión");
        System.out.println("2. Salir");
        System.out.print("Seleccione una opción: ");
        
        String opcion = scanner.nextLine();
        
        switch (opcion) {
            case "1":
                iniciarSesion();
                break;
            case "2":
                System.out.println("¡Hasta luego!");
                System.exit(0);
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }
    
    private static void iniciarSesion() {
        System.out.print("\nCorreo electrónico: ");
        String correo = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();
        
        try {
            usuarioActual = authService.login(correo, contrasena);
            if (usuarioActual == null) {
                System.out.println("Login fallido. Por favor, intente de nuevo.");
            }
        } catch (Exception e) {
            System.err.println("Error durante el login: " + e.getMessage());
        }
    }
    
    private static void mostrarMenuPrincipal() {
        System.out.println("\n=== MENÚ PRINCIPAL ===");
        System.out.println("Usuario: " + usuarioActual.getCorreo() + " (Rol: " + usuarioActual.getRol() + ")");
        System.out.println("\n1. Gestión de Usuarios");
        System.out.println("2. Gestión de Libros");
        System.out.println("3. Gestión de Préstamos");
        System.out.println("4. Cerrar Sesión");
        System.out.print("Seleccione una opción: ");
        
        String opcion = scanner.nextLine();
        
        switch (opcion) {
            case "1":
                menuGestionUsuarios();
                break;
            case "2":
                menuGestionLibros();
                break;
            case "3":
                menuGestionPrestamos();
                break;
            case "4":
                usuarioActual = null;
                System.out.println("Sesión cerrada.");
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }
    
    private static void menuGestionUsuarios() {
        System.out.println("\n=== GESTIÓN DE USUARIOS ===");
        System.out.println("1. Listar Usuarios");
        System.out.println("2. Crear Usuario");
        System.out.println("3. Buscar Usuario por Correo");
        System.out.println("4. Volver");
        System.out.print("Seleccione una opción: ");
        
        String opcion = scanner.nextLine();
        
        switch (opcion) {
            case "1":
                listarUsuarios();
                break;
            case "2":
                crearUsuario();
                break;
            case "3":
                buscarUsuarioPorCorreo();
                break;
            case "4":
                return;
            default:
                System.out.println("Opción no válida.");
        }
    }
    
    private static void listarUsuarios() {
        try {
            usuarioService.listarUsuarios();
        } catch (Exception e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
    }
    
    private static void crearUsuario() {
        System.out.print("\nCorreo del nuevo usuario: ");
        String correo = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();
        
        System.out.println("\nRoles disponibles:");
        if (usuarioActual.getRol().equalsIgnoreCase("superadmin")) {
            System.out.println("- superadmin");
            System.out.println("- admin");
            System.out.println("- normal");
        } else if (usuarioActual.getRol().equalsIgnoreCase("admin")) {
            System.out.println("- normal");
        } else {
            System.out.println("No tienes permisos para crear usuarios.");
            return;
        }
        
        System.out.print("Rol del nuevo usuario: ");
        String rol = scanner.nextLine();
        
        try {
            authService.crearUsuario(usuarioActual, correo, contrasena, rol);
        } catch (Exception e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
        }
    }
    
    private static void buscarUsuarioPorCorreo() {
        System.out.print("\nCorreo del usuario: ");
        String correo = scanner.nextLine();
        
        try {
            usuarioService.buscarUsuarioPorCorreo(correo);
        } catch (Exception e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }
    }
    
    private static void menuGestionLibros() {
        System.out.println("\n=== GESTIÓN DE LIBROS ===");
        System.out.println("1. Listar Libros");
        System.out.println("2. Agregar Libro");
        System.out.println("3. Volver");
        System.out.print("Seleccione una opción: ");
        
        String opcion = scanner.nextLine();
        
        switch (opcion) {
            case "1":
                listarLibros();
                break;
            case "2":
                menuAgregarLibro();
                break;
            case "3":
                return;
            default:
                System.out.println("Opción no válida.");
        }
    }
    
    private static void menuAgregarLibro() {
        System.out.println("\n=== AGREGAR LIBRO ===");
        System.out.println("1. Auto-completar desde API (solo ISBN)");
        System.out.println("2. Entrada manual (ISBN, título y autor)");
        System.out.println("3. Volver");
        System.out.print("Seleccione una opción: ");
        
        String opcion = scanner.nextLine();
        
        switch (opcion) {
            case "1":
                agregarLibroAutoAPI();
                break;
            case "2":
                agregarLibroManual();
                break;
            case "3":
                return;
            default:
                System.out.println("Opción no válida.");
        }
    }
    
    private static void listarLibros() {
        try {
            bibliotecaService.listarLibros();
        } catch (Exception e) {
            System.err.println("Error al listar libros: " + e.getMessage());
        }
    }
    
    private static void agregarLibroAutoAPI() {
        System.out.print("\nISBN: ");
        String isbn = scanner.nextLine();
        
        try {
            // Solo se necesita el ISBN, el resto se obtiene de la API
            Libro libro = new Libro(isbn, "", "", "disponible");
            bibliotecaService.agregarLibroAutoAPI(libro);
        } catch (Exception e) {
            System.err.println("Error al agregar libro: " + e.getMessage());
        }
    }
    
    private static void agregarLibroManual() {
        System.out.print("\nISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        
        try {
            Libro libro = new Libro(isbn, titulo, autor, "disponible");
            bibliotecaService.agregarLibroManual(libro);
        } catch (Exception e) {
            System.err.println("Error al agregar libro: " + e.getMessage());
        }
    }
    
    private static void menuGestionPrestamos() {
        System.out.println("\n=== GESTIÓN DE PRÉSTAMOS ===");
        System.out.println("1. Listar Préstamos");
        System.out.println("2. Crear Préstamo");
        System.out.println("3. Volver");
        System.out.print("Seleccione una opción: ");
        
        String opcion = scanner.nextLine();
        
        switch (opcion) {
            case "1":
                listarPrestamos();
                break;
            case "2":
                crearPrestamo();
                break;
            case "3":
                return;
            default:
                System.out.println("Opción no válida.");
        }
    }
    
    private static void listarPrestamos() {
        try {
            prestamoService.listarPrestamos();
        } catch (Exception e) {
            System.err.println("Error al listar préstamos: " + e.getMessage());
        }
    }
    
    private static void crearPrestamo() {
        try {
            System.out.print("\nID de Usuario: ");
            int usuarioId = Integer.parseInt(scanner.nextLine());
            System.out.print("ISBN del Libro: ");
            String isbn = scanner.nextLine();
            
            Prestamo prestamo = new Prestamo(usuarioId, isbn, false);
            prestamoService.crearPrestamo(prestamo);
        } catch (NumberFormatException e) {
            System.err.println("Error: El ID de usuario debe ser un número válido.");
        } catch (Exception e) {
            System.err.println("Error al crear préstamo: " + e.getMessage());
        }
    }
}

