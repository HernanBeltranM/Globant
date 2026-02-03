package Principal;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Scanner;

public class EncriptadorInteractivo {
    // La llave debe ser de 16 caracteres exactos para AES-128
    private static final String LLAVE_MAESTRA = "t3399#27@%9^$^N&";
    private static final String ALGORITMO = "AES";

    public static void main(String[] args) {
        Scanner lector = new Scanner(System.in);

        try {
            System.out.println("--- 🛡️ BIENVENIDO AL CIFRADOR " + ALGORITMO + " ---");
            System.out.print("Escribe el email o usuario a encriptar: ");
            String entrada = lector.nextLine();

            // Proceso de Cifrado
            String textoCifrado = procesar(entrada, LLAVE_MAESTRA, Cipher.ENCRYPT_MODE);
            System.out.println("\n[!] TEXTO ILEGIBLE: " + textoCifrado);

            System.out.println("\nPresiona ENTER para desencriptar y recuperar el original...");
            lector.nextLine();

            // Proceso de Descifrado
            String textoOriginal = procesar(textoCifrado, LLAVE_MAESTRA, Cipher.DECRYPT_MODE);
            System.out.println("[✔] TEXTO RECUPERADO: " + textoOriginal);

        } catch (Exception e) {
            System.out.println("Error: Asegúrate de que la llave tenga 16 caracteres.");
        } finally {
            lector.close();
        }
    }

    /**
     * Método versátil para encriptar o desencriptar según el modo
     */
    public static String procesar(String datos, String llave, int modo) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(llave.getBytes(), ALGORITMO);
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(modo, keySpec);

        if (modo == Cipher.ENCRYPT_MODE) {
            byte[] bytesCifrados = cipher.doFinal(datos.getBytes());
            return Base64.getEncoder().encodeToString(bytesCifrados);
        } else {
            byte[] bytesDecodificados = Base64.getDecoder().decode(datos);
            return new String(cipher.doFinal(bytesDecodificados));
        }
    }
}
