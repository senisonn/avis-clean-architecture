package fr.esgi.soheil.kevin.application.usecase.moderator;

import fr.esgi.soheil.kevin.application.dto.AuthRequest;
import fr.esgi.soheil.kevin.application.dto.AuthResponse;
import fr.esgi.soheil.kevin.application.port.in.ModeratorAuthenticator;
import fr.esgi.soheil.kevin.application.port.out.ModeratorRepository;
import fr.esgi.soheil.kevin.application.port.out.PasswordHasher;
import fr.esgi.soheil.kevin.application.port.out.TokenManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ModeratorAuthenticatorHandler implements ModeratorAuthenticator {

    private final ModeratorRepository moderatorRepository;
    private final PasswordHasher       passwordHasher;
    private final TokenManager         tokenManager;

    @Override
    public AuthResponse login(AuthRequest request) {
        var moderator = moderatorRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordHasher.matches(request.password(), moderator.getPassword()))
            throw new IllegalArgumentException("Invalid credentials");

        String token = tokenManager.generateToken(moderator.getEmail(), "MODERATOR");
        return new AuthResponse(token, moderator.getUsername());
    }

    @Override
    public void logout(String token) {
        tokenManager.invalidate(token);
    }
}