package fr.esgi.soheil.kevin.application.usecase.review;

import fr.esgi.soheil.kevin.application.dto.Review;
import fr.esgi.soheil.kevin.application.port.in.ReviewRejector;
import fr.esgi.soheil.kevin.application.port.out.ModeratorRepository;
import fr.esgi.soheil.kevin.application.port.out.ReviewRepository;
import fr.esgi.soheil.kevin.domain.exception.ModeratorNotFoundException;
import fr.esgi.soheil.kevin.domain.exception.ReviewNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewRejectorHandler implements ReviewRejector {

    private final ReviewRepository    reviewRepository;
    private final ModeratorRepository moderatorRepository;

    @Override
    public Review rejectReview(Long reviewId, Long moderatorId) {
        var review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        var moderator = moderatorRepository.findById(moderatorId)
                .orElseThrow(() -> new ModeratorNotFoundException(moderatorId));

        review.reject(moderator); // domain method

        return Review.fromDomain(reviewRepository.save(review));
    }
}

