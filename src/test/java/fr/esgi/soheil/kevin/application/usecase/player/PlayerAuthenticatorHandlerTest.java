package fr.esgi.soheil.kevin.application.usecase.player;

import fr.esgi.soheil.kevin.application.dto.AuthRequest;
import fr.esgi.soheil.kevin.application.port.out.PasswordHasher;
import fr.esgi.soheil.kevin.application.port.out.PlayerRepository;
import fr.esgi.soheil.kevin.application.port.out.TokenManager;
import fr.esgi.soheil.kevin.domain.model.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerAuthenticatorHandlerTest {

    @Mock PlayerRepository playerRepository;
    @Mock PasswordHasher   passwordHasher;
    @Mock TokenManager     tokenManager;

    @InjectMocks PlayerAuthenticatorHandler handler;

    private Player player() {
        Player p = new Player();
        p.setId(1L);
        p.setEmail("soheil@esgi.fr");
        p.setUsername("soheil");
        p.setPassword("$hashed$");
        return p;
    }

    @Test
    void login_shouldReturnTokenAndPlayerInfo() {
        when(playerRepository.findByEmail("soheil@esgi.fr")).thenReturn(Optional.of(player()));
        when(passwordHasher.matches("secret", "$hashed$")).thenReturn(true);
        when(tokenManager.generateToken("soheil@esgi.fr", "PLAYER")).thenReturn("jwt-token");

        var result = handler.login(new AuthRequest("soheil@esgi.fr", "secret"));

        assertThat(result.token()).isEqualTo("jwt-token");
        assertThat(result.username()).isEqualTo("soheil");
        assertThat(result.role()).isEqualTo("PLAYER");
    }

    @Test
    void login_shouldThrow_whenEmailNotFound() {
        when(playerRepository.findByEmail("unknown@esgi.fr")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.login(new AuthRequest("unknown@esgi.fr", "secret")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid credentials");

        verifyNoInteractions(passwordHasher, tokenManager);
    }

    @Test
    void login_shouldThrow_whenPasswordDoesNotMatch() {
        when(playerRepository.findByEmail("soheil@esgi.fr")).thenReturn(Optional.of(player()));
        when(passwordHasher.matches("wrong", "$hashed$")).thenReturn(false);

        assertThatThrownBy(() -> handler.login(new AuthRequest("soheil@esgi.fr", "wrong")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid credentials");

        verifyNoInteractions(tokenManager);
    }

    @Test
    void logout_shouldInvalidateToken() {
        handler.logout("jwt-token");

        verify(tokenManager).invalidate("jwt-token");
    }
}