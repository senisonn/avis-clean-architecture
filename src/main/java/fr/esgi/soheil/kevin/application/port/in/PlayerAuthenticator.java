package fr.esgi.soheil.kevin.application.port.in;

import fr.esgi.soheil.kevin.application.dto.AuthRequest;
import fr.esgi.soheil.kevin.application.dto.AuthResponse;

public interface PlayerAuthenticator {
    AuthResponse login(AuthRequest request);
    void logout(String token);
}