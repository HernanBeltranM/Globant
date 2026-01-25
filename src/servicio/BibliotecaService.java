package servicio;

import modelo.Libro;

public class BibliotecaService {
    
    public void agregarLibro(Libro libro) throws Exception {
        // Validar que el libro exista en la API de libros antes de guardarlo
        System.out.println("Buscando libro en Google Books API...");
        String infoLibro = LibrosAPIValidator.obtenerInfoLibro(libro.getIsbn());
        
        if (infoLibro == null || !LibrosAPIValidator.libroExiste(libro.getIsbn())) {
            System.out.println("ERROR: El libro con ISBN " + libro.getIsbn() + " no existe en la API de libros.");
            System.out.println("El libro NO será guardado en Supabase.");
            throw new Exception("Libro no encontrado en la API de libros. No se puede agregar.");
        }
        
        // Extraer información del libro desde la API
        String titulo = LibrosAPIValidator.extraerTitulo(infoLibro);
        String autor = LibrosAPIValidator.extraerAutores(infoLibro);
        
        // Actualizar el libro con la información obtenida de la API
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        
        System.out.println("✓ Libro encontrado en la API:");
        System.out.println("  - Título: " + titulo);
        System.out.println("  - Autor(es): " + autor);
        
        String json = String.format(
            "{\"isbn\":\"%s\",\"titulo\":\"%s\",\"autor\":\"%s\",\"estado\":\"%s\"}",
            libro.getIsbn(), titulo, autor, libro.getEstado()
        );
        String response = SupabaseClient.post(SupabaseConfig.LIBROS_URL, json);
        System.out.println("Libro agregado a Supabase exitosamente.");
    }
    
    public void listarLibros() throws Exception {
        String response = SupabaseClient.get(SupabaseConfig.LIBROS_URL);
        System.out.println("Libros: " + response);
    }
    
    public void actualizarEstadoLibro(String isbn, String nuevoEstado) throws Exception {
        String url = SupabaseConfig.LIBROS_URL + "?isbn=eq." + isbn;
        String json = String.format("{\"estado\":\"%s\"}", nuevoEstado);
        String response = SupabaseClient.patch(url, json);
        System.out.println("Estado del libro actualizado: " + response);
    }
    
    public void buscarLibro(String isbn) throws Exception {
        String url = SupabaseConfig.LIBROS_URL + "?isbn=eq." + isbn;
        String response = SupabaseClient.get(url);
        System.out.println("Libro encontrado: " + response);
    }
}
