package Principal;

import servicio.LibrosAPIValidator;

/**
 * Clase de prueba para verificar la funcionalidad de auto-completado desde la API
 */
public class TestISBNFetch {
    
    public static void main(String[] args) {
        System.out.println("=== Test de obtención de información desde Google Books API ===\n");
        
        // ISBN de ejemplo del problema: Don Quijote de la Mancha
        String isbn = "9788420412146";
        
        System.out.println("Probando con ISBN: " + isbn);
        System.out.println("Consultando API...\n");
        
        // Obtener información completa
        String info = LibrosAPIValidator.obtenerInfoLibro(isbn);
        
        if (info != null) {
            System.out.println("✓ Respuesta recibida de la API");
            
            // Extraer título
            String titulo = LibrosAPIValidator.extraerTitulo(info);
            System.out.println("Título: " + titulo);
            
            // Extraer autores
            String autores = LibrosAPIValidator.extraerAutores(info);
            System.out.println("Autor(es): " + autores);
            
            // Verificar que el libro existe
            boolean existe = LibrosAPIValidator.libroExiste(isbn);
            System.out.println("\n¿Libro existe?: " + (existe ? "Sí" : "No"));
            
        } else {
            System.out.println("✗ No se pudo obtener información de la API");
        }
        
        System.out.println("\n=== Fin de la prueba ===");
    }
}
