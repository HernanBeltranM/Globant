package servicio;

import modelo.Prestamo;

public class PrestamoService {
    
    public void crearPrestamo(Prestamo prestamo) throws Exception {
        String json = String.format(
            "{\"usuario_id\":%d,\"libro_isbn\":\"%s\",\"comprado\":%b}",
            prestamo.getUsuario_id(), prestamo.getLibro_isbn(), prestamo.getComprado()
        );
        String response = SupabaseClient.post(SupabaseConfig.PRESTAMOS_URL, json);
        System.out.println("Préstamo creado: " + response);
    }
    
    public void listarPrestamos() throws Exception {
        String response = SupabaseClient.get(SupabaseConfig.PRESTAMOS_URL);
        System.out.println("Préstamos: " + response);
    }
    
    public void aprobarPrestamo(Long idPrestamo, String fechaAprobacion, String fechaExpiracion) throws Exception {
        String url = SupabaseConfig.PRESTAMOS_URL + "?id_prestamo=eq." + idPrestamo;
        String json = String.format(
            "{\"fecha_aprobacion\":\"%s\",\"fecha_expiracion\":%s}",
            fechaAprobacion, 
            fechaExpiracion != null ? "\"" + fechaExpiracion + "\"" : "null"
        );
        String response = SupabaseClient.patch(url, json);
        System.out.println("Préstamo aprobado: " + response);
    }
    
    public void listarPrestamosPorUsuario(Integer usuarioId) throws Exception {
        String url = SupabaseConfig.PRESTAMOS_URL + "?usuario_id=eq." + usuarioId;
        String response = SupabaseClient.get(url);
        System.out.println("Préstamos del usuario: " + response);
    }
}
