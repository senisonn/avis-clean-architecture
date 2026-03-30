package fr.esgi.soheil.kevin.application.port.out;

/**
 * Output port — the application layer declares what it needs
 * from a token system, without knowing it's JWT.
 * Swap to Nimbus, Auth0, or any other lib: only JwtTokenAdapter changes.
 */
public interface TokenManager {

    String  generateToken(String subject);

    String  extractSubject(String token);

    boolean isValid(String token);

    void    invalidate(String token);
}