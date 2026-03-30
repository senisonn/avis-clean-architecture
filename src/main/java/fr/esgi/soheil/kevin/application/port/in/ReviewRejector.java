package fr.esgi.soheil.kevin.application.port.in;

import fr.esgi.soheil.kevin.application.dto.Review;

public interface ReviewRejector {
    Review rejectReview(Long reviewId, Long moderatorId);
}