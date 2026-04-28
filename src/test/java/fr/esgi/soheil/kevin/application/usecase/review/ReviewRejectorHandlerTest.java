package fr.esgi.soheil.kevin.application.usecase.review;

import fr.esgi.soheil.kevin.application.port.out.ModeratorRepository;
import fr.esgi.soheil.kevin.application.port.out.ReviewRepository;
import fr.esgi.soheil.kevin.domain.exception.ReviewNotFoundException;
import fr.esgi.soheil.kevin.domain.model.*;
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
class ReviewRejectorHandlerTest {

    @Mock ReviewRepository    reviewRepository;
    @Mock ModeratorRepository moderatorRepository;

    @InjectMocks ReviewRejectorHandler handler;

    private Review publishedReview() {
        Game game = new Game(); game.setId(1L); game.setName("Game");
        Player player = new Player(); player.setId(1L); player.setUsername("user");
        Review r = new Review();
        r.setId(1L); r.setGame(game); r.setPlayer(player);
        r.publish();
        return r;
    }

    @Test
    void rejectReview_shouldSetStatusRejected() {
        Moderator mod = new Moderator(); mod.setId(1L);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(publishedReview()));
        when(moderatorRepository.findById(1L)).thenReturn(Optional.of(mod));
        when(reviewRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = handler.rejectReview(1L, 1L);

        assertThat(result.status()).isEqualTo(ReviewStatus.REJECTED);
        verify(reviewRepository).save(any());
    }

    @Test
    void rejectReview_shouldThrow_whenAlreadyApproved() {
        Review review = publishedReview();
        Moderator mod = new Moderator(); mod.setId(1L);
        review.approve(mod);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(moderatorRepository.findById(1L)).thenReturn(Optional.of(mod));

        assertThatThrownBy(() -> handler.rejectReview(1L, 1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("approved");

        verify(reviewRepository, never()).save(any());
    }

    @Test
    void rejectReview_shouldThrow_whenReviewNotFound() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.rejectReview(99L, 1L))
                .isInstanceOf(ReviewNotFoundException.class);
    }
}