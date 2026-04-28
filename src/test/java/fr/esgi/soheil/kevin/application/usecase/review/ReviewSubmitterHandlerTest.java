package fr.esgi.soheil.kevin.application.usecase.review;

import fr.esgi.soheil.kevin.application.dto.CreateReviewCommand;
import fr.esgi.soheil.kevin.application.port.out.GameRepository;
import fr.esgi.soheil.kevin.application.port.out.PlayerRepository;
import fr.esgi.soheil.kevin.application.port.out.ReviewRepository;
import fr.esgi.soheil.kevin.domain.exception.GameNotFoundException;
import fr.esgi.soheil.kevin.domain.model.Game;
import fr.esgi.soheil.kevin.domain.model.Player;
import fr.esgi.soheil.kevin.domain.model.ReviewStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewSubmitterHandlerTest {

    @Mock ReviewRepository reviewRepository;
    @Mock GameRepository   gameRepository;
    @Mock PlayerRepository playerRepository;

    @InjectMocks ReviewSubmitterHandler handler;

    @Test
    void submitReview_shouldSaveReviewWithStatusPublished() {
        Game game = new Game();
        game.setId(1L);
        game.setName("Zelda");

        Player player = new Player();
        player.setId(2L);
        player.setUsername("soheil");

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(playerRepository.findById(2L)).thenReturn(Optional.of(player));
        when(reviewRepository.save(any())).thenAnswer(inv -> {
            fr.esgi.soheil.kevin.domain.model.Review r = inv.getArgument(0);
            r.setId(10L);
            return r;
        });

        var result = handler.submitReview(new CreateReviewCommand(1L, 2L, "Top jeu", 4.5f));

        assertThat(result.status()).isEqualTo(ReviewStatus.PUBLISHED);
        assertThat(result.gameName()).isEqualTo("Zelda");
        assertThat(result.playerUsername()).isEqualTo("soheil");
        verify(reviewRepository).save(any());
    }

    @Test
    void submitReview_shouldThrow_whenGameNotFound() {
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.submitReview(new CreateReviewCommand(99L, 1L, "Top", 4.0f)))
                .isInstanceOf(GameNotFoundException.class);

        verifyNoInteractions(reviewRepository);
    }

    @Test
    void submitReview_shouldThrow_whenPlayerNotFound() {
        Game game = new Game();
        game.setId(1L);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.submitReview(new CreateReviewCommand(1L, 99L, "Top", 4.0f)))
                .isInstanceOf(RuntimeException.class);

        verifyNoInteractions(reviewRepository);
    }
}