package fr.esgi.soheil.kevin.application.usecase.review;

import fr.esgi.soheil.kevin.application.dto.Review;
import fr.esgi.soheil.kevin.application.port.in.ReviewApprover;
import fr.esgi.soheil.kevin.application.port.out.ModeratorRepository;
import fr.esgi.soheil.kevin.application.port.out.ReviewRepository;
import fr.esgi.soheil.kevin.domain.exception.ReviewNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewApproverHandler implements ReviewApprover {

    private final ReviewRepository    reviewRepository;
    private final ModeratorRepository moderatorRepository;

    @Override
    public Review approveReview(Long reviewId, Long moderatorId) {
        var review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        var moderator = moderatorRepository.findById(moderatorId)
                .orElseThrow(() -> new ReviewNotFoundException(moderatorId));

        review.approve(moderator);

        return Review.fromDomain(reviewRepository.save(review));
    }
}

