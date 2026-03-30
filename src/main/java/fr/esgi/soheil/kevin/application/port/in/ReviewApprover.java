package fr.esgi.soheil.kevin.application.port.in;

import fr.esgi.soheil.kevin.application.dto.Review;

public interface ReviewApprover {
    Review approveReview(Long reviewId, Long moderatorId);
}