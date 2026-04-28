package fr.esgi.soheil.kevin.application.usecase.review;

import fr.esgi.soheil.kevin.application.port.out.ReviewRepository;
import fr.esgi.soheil.kevin.domain.exception.ReviewNotFoundException;
import fr.esgi.soheil.kevin.domain.model.Game;
import fr.esgi.soheil.kevin.domain.model.Player;
import fr.esgi.soheil.kevin.domain.model.Review;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewDeleterHandlerTest {

    @Mock ReviewRepository reviewRepository;

    @InjectMocks ReviewDeleterHandler handler;

    private Review reviewOwnedBy(Long playerId) {
        Player player = new Player();
        player.setId(playerId);
        Game game = new Game(); game.setId(1L);
        Review review = new Review();
        review.setId(10L);
        review.setPlayer(player);
        review.setGame(game);
        return review;
    }

    @Test
    void deleteReview_shouldDelete_whenPlayerOwnsReview() {
        when(reviewRepository.findById(10L)).thenReturn(Optional.of(reviewOwnedBy(2L)));

        handler.deleteReview(10L, 2L);

        verify(reviewRepository).deleteById(10L);
    }

    @Test
    void deleteReview_shouldThrow_whenPlayerDoesNotOwnReview() {
        when(reviewRepository.findById(10L)).thenReturn(Optional.of(reviewOwnedBy(2L)));

        assertThatThrownBy(() -> handler.deleteReview(10L, 999L))
                .isInstanceOf(SecurityException.class);

        verify(reviewRepository, never()).deleteById(any());
    }

    @Test
    void deleteReview_shouldThrow_whenReviewNotFound() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.deleteReview(99L, 1L))
                .isInstanceOf(ReviewNotFoundException.class);
    }
}