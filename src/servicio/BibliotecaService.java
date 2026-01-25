package servicio;

import modelo.Libro;

public class BibliotecaService {
    
    /**
     * Escapa caracteres especiales para JSON
     * @param text Texto a escapar
     * @return Texto escapado para JSON
     */
    private String escaparJSON(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
    
    /**
     * Agrega un libro con auto-completado desde la API (solo requiere ISBN)
     * @param libro Libro con ISBN, el título y autor se obtienen de la API
     * @throws Exception si el libro no existe en la API
     */
    public void agregarLibroAutoAPI(Libro libro) throws Exception {
        // Validar que el libro exista en la API de libros antes de guardarlo
        System.out.println("Buscando libro en Google Books API...");
        String infoLibro = LibrosAPIValidator.obtenerInfoLibro(libro.getIsbn());
        
        if (infoLibro == null) {
            System.out.println("ERROR: El libro con ISBN " + libro.getIsbn() + " no existe en la API de libros.");
            System.out.println("El libro NO será guardado en Supabase.");
            throw new Exception("Libro no encontrado en la API de libros. No se puede agregar.");
        }
        
        // Verificar que tenga resultados (totalItems > 0)
        if (!infoLibro.contains("\"totalItems\":") || !LibrosAPIValidator.tieneResultados(infoLibro)) {
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
        
        // Escapar strings para JSON
        String tituloEscapado = escaparJSON(titulo);
        String autorEscapado = escaparJSON(autor);
        
        String json = String.format(
            "{\"isbn\":\"%s\",\"titulo\":\"%s\",\"autor\":\"%s\",\"estado\":\"%s\"}",
            libro.getIsbn(), tituloEscapado, autorEscapado, libro.getEstado()
        );
        String response = SupabaseClient.post(SupabaseConfig.LIBROS_URL, json);
        System.out.println("Libro agregado a Supabase exitosamente.");
    }
    
    /**
     * Agrega un libro con entrada manual (ISBN, título y autor proporcionados por el usuario)
     * @param libro Libro con ISBN, título y autor proporcionados manualmente
     * @throws Exception si el libro no existe en la API
     */
    public void agregarLibroManual(Libro libro) throws Exception {
        // Validar que el libro exista en la API de libros antes de guardarlo
        System.out.println("Validando libro en API externa...");
        boolean existe = LibrosAPIValidator.libroExiste(libro.getIsbn());
        
        if (!existe) {
            System.out.println("ERROR: El libro con ISBN " + libro.getIsbn() + " no existe en la API de libros.");
            System.out.println("El libro NO será guardado en Supabase.");
            throw new Exception("Libro no encontrado en la API de libros. No se puede agregar.");
        }
        
        System.out.println("✓ Libro validado correctamente en la API.");
        
        // Escapar strings para JSON
        String tituloEscapado = escaparJSON(libro.getTitulo());
        String autorEscapado = escaparJSON(libro.getAutor());
        
        String json = String.format(
            "{\"isbn\":\"%s\",\"titulo\":\"%s\",\"autor\":\"%s\",\"estado\":\"%s\"}",
            libro.getIsbn(), tituloEscapado, autorEscapado, libro.getEstado()
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
