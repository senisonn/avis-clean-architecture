package fr.esgi.soheil.kevin.application.usecase.review;

import fr.esgi.soheil.kevin.application.dto.Review;
import fr.esgi.soheil.kevin.application.port.in.PendingReviewFinder;
import fr.esgi.soheil.kevin.application.port.out.ReviewRepository;
import fr.esgi.soheil.kevin.domain.model.ReviewStatus;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class PendingReviewFinderHandler implements PendingReviewFinder {

    private final ReviewRepository reviewRepository;

    @Override
    public List<Review> getPendingReviews() {
        return reviewRepository.findByStatus(ReviewStatus.PUBLISHED)
                .stream().map(Review::fromDomain).toList();
    }
}

