package servicio;

import modelo.Libro;

public class BibliotecaService {
    
    public void agregarLibro(Libro libro) throws Exception {
        // Validar que el libro exista en la API de libros antes de guardarlo
        System.out.println("Validando libro en API externa...");
        boolean existe = LibrosAPIValidator.libroExiste(libro.getIsbn());
        
        if (!existe) {
            System.out.println("ERROR: El libro con ISBN " + libro.getIsbn() + " no existe en la API de libros.");
            System.out.println("El libro NO será guardado en Supabase.");
            throw new Exception("Libro no encontrado en la API de libros. No se puede agregar.");
        }
        
        System.out.println("✓ Libro validado correctamente en la API.");
        String json = String.format(
            "{\"isbn\":\"%s\",\"titulo\":\"%s\",\"autor\":\"%s\",\"estado\":\"%s\"}",
            libro.getIsbn(), libro.getTitulo(), libro.getAutor(), libro.getEstado()
        );
        String response = SupabaseClient.post(SupabaseConfig.LIBROS_URL, json);
        System.out.println("Libro agregado a Supabase: " + response);
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
