package fr.esgi.soheil.kevin.application.dto;

/**
 * Returned after a successful login or register.
 */
public record AuthResponse(
        String token,
        String username
) {}