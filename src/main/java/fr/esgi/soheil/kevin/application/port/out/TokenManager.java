package fr.esgi.soheil.kevin.application.port.out;

/**
 * Output port for token operations.
 * Current implementation: JwtTokenAdapter (JJWT).
 * Swappable without touching any handler or controller.
 */
public interface TokenManager {
    String  generateToken(String subject, String role);
    String  extractSubject(String token);
    String  extractRole(String token);
    boolean isValid(String token);
    void    invalidate(String token);
}