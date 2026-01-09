package servicio;

public class SupabaseConfig {
    public static final String BASE_URL = "https://sgmjnvhnxjlmbkzrenfg.supabase.co/rest/v1";
    public static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNnbWpudmhueGpsbWJrenJlbmZnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Njc5MDk0MTUsImV4cCI6MjA4MzQ4NTQxNX0.g3AFg_YSRW-jgPH0BHvhdQ5Hz4KvDeGRCZsKcAgwyco";
    
    public static final String USUARIOS_URL = BASE_URL + "/usuarios";
    public static final String LIBROS_URL = BASE_URL + "/libros";
    public static final String PRESTAMOS_URL = BASE_URL + "/prestamos";
    public static final String ADQUISICIONES_URL = BASE_URL + "/adquisiciones";
}
