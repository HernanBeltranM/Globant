package servicio;

public class SupabaseConfig {
    public static final String BASE_URL = getRequiredEnv("URL_BASE_DE_DATOS");
    public static final String API_KEY = getRequiredEnv("API_KEY_BASE_DE_DATOS");
    
    public static final String USUARIOS_URL = BASE_URL + "/usuarios";
    public static final String LIBROS_URL = BASE_URL + "/libros";
    public static final String PRESTAMOS_URL = BASE_URL + "/prestamos";
    public static final String ADQUISICIONES_URL = BASE_URL + "/adquisiciones";

    private static String getRequiredEnv(String name) {
        String value = System.getenv(name);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException(
                "Falta variable de entorno requerida: " + name +
                ". Configurala en 1Password (entorno PROLLECLIBROS) antes de ejecutar la app."
            );
        }
        return value.trim();
    }
}
