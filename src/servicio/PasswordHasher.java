package servicio;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Password hashing utility using SHA-256 with salt.
 * 
 * NOTE: For production systems, consider using dedicated password hashing algorithms
 * like bcrypt, scrypt, or Argon2, which are designed to be computationally expensive
 * and resistant to brute force attacks. This implementation uses SHA-256 which is
 * faster but less secure for password hashing purposes.
 */
public class PasswordHasher {
    
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;
    
    /**
     * Hash a password with a generated salt
     */
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        byte[] salt = generateSalt();
        byte[] hash = hashWithSalt(password, salt);
        
        // Combine salt and hash, encode as Base64
        byte[] combined = new byte[salt.length + hash.length];
        System.arraycopy(salt, 0, combined, 0, salt.length);
        System.arraycopy(hash, 0, combined, salt.length, hash.length);
        
        return Base64.getEncoder().encodeToString(combined);
    }
    
    /**
     * Verify a password against a stored hash
     */
    public static boolean verifyPassword(String password, String storedHash) throws NoSuchAlgorithmException {
        byte[] combined = Base64.getDecoder().decode(storedHash);
        
        // Extract salt and hash
        byte[] salt = new byte[SALT_LENGTH];
        byte[] hash = new byte[combined.length - SALT_LENGTH];
        System.arraycopy(combined, 0, salt, 0, SALT_LENGTH);
        System.arraycopy(combined, SALT_LENGTH, hash, 0, hash.length);
        
        // Hash the provided password with the extracted salt
        byte[] testHash = hashWithSalt(password, salt);
        
        // Compare hashes
        return MessageDigest.isEqual(hash, testHash);
    }
    
    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }
    
    private static byte[] hashWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        md.update(salt);
        return md.digest(password.getBytes());
    }
}
