package fr.esgi.soheil.kevin.application.port.in;

import fr.esgi.soheil.kevin.application.dto.CreateReviewCommand;
import fr.esgi.soheil.kevin.application.dto.Review;

public interface ReviewSubmitter {
    Review submitReview(CreateReviewCommand command);
}