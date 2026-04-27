package fr.esgi.soheil.kevin.application.usecase.player;

import fr.esgi.soheil.kevin.application.dto.AuthRequest;
import fr.esgi.soheil.kevin.application.dto.AuthResponse;
import fr.esgi.soheil.kevin.application.port.in.PlayerAuthenticator;
import fr.esgi.soheil.kevin.application.port.out.PasswordHasher;
import fr.esgi.soheil.kevin.application.port.out.PlayerRepository;
import fr.esgi.soheil.kevin.application.port.out.TokenManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerAuthenticatorHandler implements PlayerAuthenticator {

    private final PlayerRepository playerRepository;
    private final PasswordHasher   passwordHasher;
    private final TokenManager     tokenManager;

    @Override
    public AuthResponse login(AuthRequest request) {
        var player = playerRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordHasher.matches(request.password(), player.getPassword()))
            throw new IllegalArgumentException("Invalid credentials");

        String token = tokenManager.generateToken(player.getEmail(), "PLAYER");
        return new AuthResponse(token, player.getId(), player.getUsername(), "PLAYER");
    }

    @Override
    public void logout(String token) {
        tokenManager.invalidate(token);
    }
}

