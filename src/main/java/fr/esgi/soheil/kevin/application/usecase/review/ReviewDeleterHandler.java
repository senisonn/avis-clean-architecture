package fr.esgi.soheil.kevin.application.usecase.review;

import fr.esgi.soheil.kevin.application.port.in.ReviewDeleter;
import fr.esgi.soheil.kevin.application.port.out.ReviewRepository;
import fr.esgi.soheil.kevin.domain.exception.ReviewNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewDeleterHandler implements ReviewDeleter {

    private final ReviewRepository reviewRepository;

    @Override
    public void deleteReview(Long reviewId, Long playerId) {
        fr.esgi.soheil.kevin.domain.model.Review review =
                reviewRepository.findById(reviewId)
                        .orElseThrow(() -> new ReviewNotFoundException(reviewId));

        if (!review.getPlayer().getId().equals(playerId))
            throw new SecurityException("Player " + playerId + " cannot delete this review");

        reviewRepository.deleteById(reviewId);
    }
}

