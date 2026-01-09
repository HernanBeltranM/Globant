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
    
    /**
     * Valida si un libro existe en la API de Google Books usando su ISBN
     * @param isbn El ISBN del libro a validar
     * @return true si el libro existe en la API, false en caso contrario
     */
    public static boolean libroExiste(String isbn) {
        try {
            String cleanIsbn = isbn.replaceAll("[^0-9X]", "");
            String urlString = GOOGLE_BOOKS_API + URLEncoder.encode(cleanIsbn, StandardCharsets.UTF_8.toString());
            
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
            String urlString = GOOGLE_BOOKS_API + URLEncoder.encode(cleanIsbn, StandardCharsets.UTF_8.toString());
            
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
     * Extrae el resumen/descripción del libro desde la respuesta de la API
     * @param apiResponse Respuesta JSON de Google Books API
     * @return El resumen del libro o un mensaje indicando que no está disponible
     */
    public static String extraerResumen(String apiResponse) {
        if (apiResponse == null || !apiResponse.contains("\"description\"")) {
            return "Resumen no disponible";
        }
        
        try {
            int startIndex = apiResponse.indexOf("\"description\":\"");
            if (startIndex == -1) {
                return "Resumen no disponible";
            }
            
            startIndex += 15;
            if (startIndex >= apiResponse.length()) {
                return "Resumen no disponible";
            }
            
            int endIndex = apiResponse.indexOf("\"", startIndex);
            
            // Handle escaped quotes
            while (endIndex > 0 && apiResponse.charAt(endIndex - 1) == '\\') {
                endIndex = apiResponse.indexOf("\"", endIndex + 1);
            }
            
            if (endIndex == -1 || endIndex <= startIndex) {
                return "Resumen no disponible";
            }
            
            String description = apiResponse.substring(startIndex, endIndex);
            // Unescape common characters
            description = description.replace("\\n", "\n")
                                   .replace("\\\"", "\"")
                                   .replace("\\\\", "\\");
            
            // Limit length for display
            if (description.length() > 500) {
                description = description.substring(0, 500) + "...";
            }
            
            return description;
        } catch (Exception e) {
            return "Resumen no disponible";
        }
    }
    
    /**
     * Extrae el link de compra del libro desde la respuesta de la API
     * @param apiResponse Respuesta JSON de Google Books API
     * @return El link de compra o null si no está disponible
     */
    public static String extraerLinkCompra(String apiResponse) {
        if (apiResponse == null) {
            return null;
        }
        
        try {
            // Try to find the "infoLink" which is the Google Books page
            if (apiResponse.contains("\"infoLink\"")) {
                int startIndex = apiResponse.indexOf("\"infoLink\":\"");
                if (startIndex != -1) {
                    startIndex += 12;
                    if (startIndex < apiResponse.length()) {
                        int endIndex = apiResponse.indexOf("\"", startIndex);
                        if (endIndex != -1 && endIndex > startIndex) {
                            return apiResponse.substring(startIndex, endIndex).replace("\\", "");
                        }
                    }
                }
            }
            
            // Try to find the "canonicalVolumeLink"
            if (apiResponse.contains("\"canonicalVolumeLink\"")) {
                int startIndex = apiResponse.indexOf("\"canonicalVolumeLink\":\"");
                if (startIndex != -1) {
                    startIndex += 23;
                    if (startIndex < apiResponse.length()) {
                        int endIndex = apiResponse.indexOf("\"", startIndex);
                        if (endIndex != -1 && endIndex > startIndex) {
                            return apiResponse.substring(startIndex, endIndex).replace("\\", "");
                        }
                    }
                }
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Verifica si el libro tiene PDF completo disponible
     * @param apiResponse Respuesta JSON de Google Books API
     * @return true si tiene PDF completo, false en caso contrario
     */
    public static boolean tienePDFCompleto(String apiResponse) {
        if (apiResponse == null) {
            return false;
        }
        
        // Check if there's a PDF download available or full viewability
        // Handle both "pdf":{"isAvailable":true and "pdf": {"isAvailable": true
        boolean hasPdfDownload = apiResponse.contains("\"pdf\"") && 
                                 (apiResponse.contains("\"isAvailable\":true") || 
                                  apiResponse.contains("\"isAvailable\": true"));
        boolean isFullView = apiResponse.contains("\"viewability\":\"ALL_PAGES\"") ||
                            apiResponse.contains("\"viewability\": \"ALL_PAGES\"");
        
        return hasPdfDownload || isFullView;
    }
    
    /**
     * Muestra información detallada del libro
     * @param isbn El ISBN del libro
     */
    public static void mostrarInfoLibro(String isbn) {
        String infoLibro = obtenerInfoLibro(isbn);
        
        if (infoLibro == null || !infoLibro.contains("\"totalItems\"") || 
            infoLibro.contains("\"totalItems\":0")) {
            System.out.println("No se encontró información para el libro con ISBN: " + isbn);
            return;
        }
        
        System.out.println("\n=== INFORMACIÓN DEL LIBRO ===");
        
        // Extract and show title
        String titulo = extraerCampo(infoLibro, "title");
        if (titulo != null) {
            System.out.println("Título: " + titulo);
        }
        
        // Extract and show authors
        String autores = extraerCampo(infoLibro, "authors");
        if (autores != null) {
            System.out.println("Autor(es): " + autores);
        }
        
        // Show summary
        String resumen = extraerResumen(infoLibro);
        System.out.println("\nResumen:");
        System.out.println(resumen);
        
        // Check PDF availability
        boolean tienePDF = tienePDFCompleto(infoLibro);
        
        if (tienePDF) {
            System.out.println("\n✓ Este libro tiene PDF completo disponible");
        } else {
            // Show purchase link only if no full PDF
            String linkCompra = extraerLinkCompra(infoLibro);
            if (linkCompra != null) {
                System.out.println("\nLink de compra: " + linkCompra);
            }
        }
        
        System.out.println("================================\n");
    }
    
    /**
     * Helper method to extract a field from JSON response
     * @param json The JSON string
     * @param field The field name
     * @return The field value or null
     */
    private static String extraerCampo(String json, String field) {
        if (json == null || !json.contains("\"" + field + "\"")) {
            return null;
        }
        
        try {
            int startIndex = json.indexOf("\"" + field + "\":");
            if (startIndex == -1) return null;
            
            startIndex = json.indexOf(":", startIndex);
            if (startIndex == -1) return null;
            startIndex++;
            
            // Skip whitespace
            while (startIndex < json.length() && Character.isWhitespace(json.charAt(startIndex))) {
                startIndex++;
            }
            
            if (startIndex >= json.length()) return null;
            
            if (json.charAt(startIndex) == '"') {
                startIndex++;
                int endIndex = json.indexOf("\"", startIndex);
                if (endIndex == -1 || endIndex <= startIndex) return null;
                return json.substring(startIndex, endIndex);
            } else if (json.charAt(startIndex) == '[') {
                // It's an array, extract first element
                int firstQuote = json.indexOf("\"", startIndex);
                if (firstQuote == -1) return null;
                startIndex = firstQuote + 1;
                int endIndex = json.indexOf("\"", startIndex);
                if (endIndex == -1 || endIndex <= startIndex) return null;
                return json.substring(startIndex, endIndex);
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
