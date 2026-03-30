package fr.esgi.soheil.kevin.application.port.out;

import fr.esgi.soheil.kevin.domain.model.Review;
import fr.esgi.soheil.kevin.domain.model.ReviewStatus;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    List<Review>     findByGameId(Long gameId);
    List<Review>     findByPlayerId(Long playerId);
    List<Review>     findByStatus(ReviewStatus status);
    Optional<Review> findById(Long id);
    Review           save(Review review);
    void             deleteById(Long id);
}