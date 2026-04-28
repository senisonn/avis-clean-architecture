package fr.esgi.soheil.kevin.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ReviewTest {

    private Review review;
    private Moderator moderator;

    @BeforeEach
    void setUp() {
        Game game = new Game();
        game.setId(1L);
        game.setName("Zelda");

        Player player = new Player();
        player.setId(1L);
        player.setUsername("soheil");

        review = new Review();
        review.setContent("Super jeu");
        review.setRating(4.5f);
        review.setGame(game);
        review.setPlayer(player);

        moderator = new Moderator();
        moderator.setId(1L);
        moderator.setUsername("mod1");
    }

    // ── publish() ────────────────────────────────────────────────

    @Test
    void publish_shouldTransitionDraftToPublished() {
        review.publish();

        assertThat(review.getStatus()).isEqualTo(ReviewStatus.PUBLISHED);
        assertThat(review.getSubmittedAt()).isNotNull();
    }

    @Test
    void publish_shouldThrow_whenAlreadyPublished() {
        review.publish();

        assertThatThrownBy(review::publish)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("DRAFT");
    }

    @Test
    void publish_shouldThrow_whenApproved() {
        review.publish();
        review.approve(moderator);

        assertThatThrownBy(review::publish)
                .isInstanceOf(IllegalStateException.class);
    }

    // ── approve() ────────────────────────────────────────────────

    @Test
    void approve_shouldTransitionPublishedToApproved() {
        review.publish();
        review.approve(moderator);

        assertThat(review.getStatus()).isEqualTo(ReviewStatus.APPROVED);
        assertThat(review.getModerator()).isEqualTo(moderator);
    }

    @Test
    void approve_shouldThrow_whenStatusIsDraft() {
        assertThatThrownBy(() -> review.approve(moderator))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("PUBLISHED");
    }

    @Test
    void approve_shouldThrow_whenAlreadyRejected() {
        review.publish();
        review.reject(moderator);

        assertThatThrownBy(() -> review.approve(moderator))
                .isInstanceOf(IllegalStateException.class);
    }

    // ── reject() ─────────────────────────────────────────────────

    @Test
    void reject_shouldTransitionPublishedToRejected() {
        review.publish();
        review.reject(moderator);

        assertThat(review.getStatus()).isEqualTo(ReviewStatus.REJECTED);
        assertThat(review.getModerator()).isEqualTo(moderator);
    }

    @Test
    void reject_shouldThrow_whenAlreadyApproved() {
        review.publish();
        review.approve(moderator);

        assertThatThrownBy(() -> review.reject(moderator))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("approved");
    }

    @Test
    void reject_canRejectDraftDirectly() {
        review.reject(moderator);

        assertThat(review.getStatus()).isEqualTo(ReviewStatus.REJECTED);
    }
}