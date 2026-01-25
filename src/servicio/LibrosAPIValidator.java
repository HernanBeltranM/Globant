package servicio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class LibrosAPIValidator {
    
    // Google Books API para validar libros por ISBN
    private static final String GOOGLE_BOOKS_API = "https://www.googleapis.com/books/v1/volumes?q=isbn:";
    private static final String API_KEY = "AIzaSyCcW8aoNU4ikuYUtq7_wyc3HGIpbFnCs8M";
    
    /**
     * Valida si un libro existe en la API de Google Books usando su ISBN
     * @param isbn El ISBN del libro a validar
     * @return true si el libro existe en la API, false en caso contrario
     */
    public static boolean libroExiste(String isbn) {
        try {
            String cleanIsbn = isbn.replaceAll("[^0-9X]", "");
            String urlString = GOOGLE_BOOKS_API + URLEncoder.encode(cleanIsbn, StandardCharsets.UTF_8.toString()) + "&key=" + API_KEY;
            
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            
            int responseCode = conn.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                String responseStr = response.toString();
                
                // Verificar si hay resultados en la respuesta
                if (responseStr.contains("\"totalItems\"")) {
                    // Extraer el valor de totalItems
                    int startIndex = responseStr.indexOf("\"totalItems\":") + 13;
                    int endIndex = responseStr.indexOf(",", startIndex);
                    if (endIndex == -1) {
                        endIndex = responseStr.indexOf("}", startIndex);
                    }
                    String totalItemsStr = responseStr.substring(startIndex, endIndex).trim();
                    int totalItems = Integer.parseInt(totalItemsStr);
                    
                    return totalItems > 0;
                }
            }
            
            return false;
            
        } catch (Exception e) {
            System.err.println("Error al validar libro en API: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene información del libro desde la API de Google Books
     * @param isbn El ISBN del libro
     * @return JSON con la información del libro o null si no existe
     */
    public static String obtenerInfoLibro(String isbn) {
        try {
            String cleanIsbn = isbn.replaceAll("[^0-9X]", "");
            String urlString = GOOGLE_BOOKS_API + URLEncoder.encode(cleanIsbn, StandardCharsets.UTF_8.toString()) + "&key=" + API_KEY;
            
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            
            int responseCode = conn.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                return response.toString();
            }
            
            return null;
            
        } catch (Exception e) {
            System.err.println("Error al obtener información del libro: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Extrae el título de la respuesta JSON de la API
     * @param jsonResponse Respuesta JSON de la API
     * @return El título del libro o "Título no disponible"
     */
    public static String extraerTitulo(String jsonResponse) {
        try {
            int titleIndex = jsonResponse.indexOf("\"title\":");
            if (titleIndex != -1) {
                int startQuote = jsonResponse.indexOf("\"", titleIndex + 8);
                int endQuote = jsonResponse.indexOf("\"", startQuote + 1);
                return jsonResponse.substring(startQuote + 1, endQuote);
            }
        } catch (Exception e) {
            System.err.println("Error al extraer título: " + e.getMessage());
        }
        return "Título no disponible";
    }
    
    /**
     * Extrae los autores de la respuesta JSON de la API
     * @param jsonResponse Respuesta JSON de la API
     * @return Los autores del libro separados por coma, o "Autor no disponible"
     */
    public static String extraerAutores(String jsonResponse) {
        try {
            int authorsIndex = jsonResponse.indexOf("\"authors\":");
            if (authorsIndex != -1) {
                int startBracket = jsonResponse.indexOf("[", authorsIndex);
                int endBracket = jsonResponse.indexOf("]", startBracket);
                String authorsArray = jsonResponse.substring(startBracket + 1, endBracket);
                
                // Extraer todos los autores del array
                StringBuilder autores = new StringBuilder();
                int currentPos = 0;
                while (true) {
                    int startQuote = authorsArray.indexOf("\"", currentPos);
                    if (startQuote == -1) break;
                    int endQuote = authorsArray.indexOf("\"", startQuote + 1);
                    if (endQuote == -1) break;
                    
                    if (autores.length() > 0) {
                        autores.append(", ");
                    }
                    autores.append(authorsArray.substring(startQuote + 1, endQuote));
                    currentPos = endQuote + 1;
                }
                
                return autores.length() > 0 ? autores.toString() : "Autor no disponible";
            }
        } catch (Exception e) {
            System.err.println("Error al extraer autores: " + e.getMessage());
        }
        return "Autor no disponible";
    }
}
