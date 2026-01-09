package servicio;

import modelo.Libro;

public class BibliotecaService {
    
    public void agregarLibro(Libro libro) throws Exception {
        String json = String.format(
            "{\"isbn\":\"%s\",\"titulo\":\"%s\",\"autor\":\"%s\",\"estado\":\"%s\"}",
            libro.getIsbn(), libro.getTitulo(), libro.getAutor(), libro.getEstado()
        );
        String response = SupabaseClient.post(SupabaseConfig.LIBROS_URL, json);
        System.out.println("Libro agregado: " + response);
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
