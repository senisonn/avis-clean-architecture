package fr.esgi.soheil.kevin.application.usecase.review;

import fr.esgi.soheil.kevin.application.port.out.ModeratorRepository;
import fr.esgi.soheil.kevin.application.port.out.ReviewRepository;
import fr.esgi.soheil.kevin.domain.exception.ModeratorNotFoundException;
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
class ReviewApproverHandlerTest {

    @Mock ReviewRepository    reviewRepository;
    @Mock ModeratorRepository moderatorRepository;

    @InjectMocks ReviewApproverHandler handler;

    private Review publishedReview() {
        Game game = new Game();
        game.setId(1L);
        game.setName("Elden Ring");

        Player player = new Player();
        player.setId(1L);
        player.setUsername("kevin");

        Review review = new Review();
        review.setId(1L);
        review.setContent("Chef-d'œuvre");
        review.setRating(5.0f);
        review.setGame(game);
        review.setPlayer(player);
        review.publish();
        return review;
    }

    @Test
    void approveReview_shouldSetStatusApproved() {
        Review review = publishedReview();
        Moderator mod = new Moderator();
        mod.setId(5L);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(moderatorRepository.findById(5L)).thenReturn(Optional.of(mod));
        when(reviewRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = handler.approveReview(1L, 5L);

        assertThat(result.status()).isEqualTo(ReviewStatus.APPROVED);
        verify(reviewRepository).save(any());
    }

    @Test
    void approveReview_shouldThrow_whenReviewNotFound() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.approveReview(99L, 1L))
                .isInstanceOf(ReviewNotFoundException.class);

        verifyNoInteractions(moderatorRepository);
    }

    @Test
    void approveReview_shouldThrow_whenModeratorNotFound() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(publishedReview()));
        when(moderatorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.approveReview(1L, 99L))
                .isInstanceOf(ModeratorNotFoundException.class);

        verify(reviewRepository, never()).save(any());
    }

    @Test
    void approveReview_shouldThrow_whenReviewIsNotPublished() {
        Review draft = new Review();
        draft.setId(2L);
        Game g = new Game(); g.setId(1L);
        Player p = new Player(); p.setId(1L);
        draft.setGame(g);
        draft.setPlayer(p);

        Moderator mod = new Moderator();
        mod.setId(1L);

        when(reviewRepository.findById(2L)).thenReturn(Optional.of(draft));
        when(moderatorRepository.findById(1L)).thenReturn(Optional.of(mod));

        assertThatThrownBy(() -> handler.approveReview(2L, 1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("PUBLISHED");
    }
}