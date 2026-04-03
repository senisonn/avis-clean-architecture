package fr.esgi.soheil.kevin.application.usecase.player;

import fr.esgi.soheil.kevin.application.dto.AuthResponse;
import fr.esgi.soheil.kevin.application.dto.RegisterCommand;
import fr.esgi.soheil.kevin.application.port.in.PlayerRegistrar;
import fr.esgi.soheil.kevin.application.port.out.PasswordHasher;
import fr.esgi.soheil.kevin.application.port.out.PlayerRepository;
import fr.esgi.soheil.kevin.application.port.out.TokenManager;
import fr.esgi.soheil.kevin.domain.model.Player;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerRegistrarHandler implements PlayerRegistrar {

    private final PlayerRepository playerRepository;
    private final PasswordHasher   passwordHasher;
    private final TokenManager     tokenManager;

    @Override
    public AuthResponse register(RegisterCommand command) {
        if (playerRepository.existsByEmail(command.email()))
            throw new IllegalStateException("Email already in use: " + command.email());

        Player player = new Player();
        player.setUsername(command.username());
        player.setEmail(command.email());
        player.setPassword(passwordHasher.hash(command.password()));
        player.setBirthDate(command.birthDate());

        Player saved = playerRepository.save(player);
        String token = tokenManager.generateToken(saved.getEmail());

        return new AuthResponse(token, saved.getUsername());
    }
}

