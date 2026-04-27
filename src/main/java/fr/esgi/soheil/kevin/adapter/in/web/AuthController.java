package fr.esgi.soheil.kevin.adapter.in.web;

import fr.esgi.soheil.kevin.application.dto.AuthRequest;
import fr.esgi.soheil.kevin.application.dto.AuthResponse;
import fr.esgi.soheil.kevin.application.dto.RegisterCommand;
import fr.esgi.soheil.kevin.application.port.in.ModeratorAuthenticator;
import fr.esgi.soheil.kevin.application.port.in.PlayerAuthenticator;
import fr.esgi.soheil.kevin.application.port.in.PlayerRegistrar;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PlayerAuthenticator    playerAuthenticator;
    private final PlayerRegistrar        playerRegistrar;
    private final ModeratorAuthenticator moderatorAuthenticator;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(playerAuthenticator.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(playerRegistrar.register(command));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.replace("Bearer ", "");
        playerAuthenticator.logout(token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/moderator/login")
    public ResponseEntity<AuthResponse> moderatorLogin(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(moderatorAuthenticator.login(request));
    }

    @PostMapping("/moderator/logout")
    public ResponseEntity<Void> moderatorLogout(
            @RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.replace("Bearer ", "");
        moderatorAuthenticator.logout(token);
        return ResponseEntity.noContent().build();
    }
}

