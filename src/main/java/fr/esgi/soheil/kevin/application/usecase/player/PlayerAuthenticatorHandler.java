package fr.esgi.soheil.kevin.application.usecase.player;

import fr.esgi.soheil.kevin.application.dto.AuthRequest;
import fr.esgi.soheil.kevin.application.dto.AuthResponse;
import fr.esgi.soheil.kevin.application.port.in.PlayerAuthenticator;
import fr.esgi.soheil.kevin.application.port.out.PlayerRepository;
import fr.esgi.soheil.kevin.application.port.out.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class PlayerAuthenticatorHandler implements PlayerAuthenticator {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder  passwordEncoder;
    private final TokenManager     tokenManager;

    @Override
    public AuthResponse login(AuthRequest request) {
        var player = playerRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), player.getPassword()))
            throw new IllegalArgumentException("Invalid credentials");

        String token = tokenManager.generateToken(player.getEmail());
        return new AuthResponse(token, player.getUsername());
    }

    @Override
    public void logout(String token) {
        tokenManager.invalidate(token);
    }
}

