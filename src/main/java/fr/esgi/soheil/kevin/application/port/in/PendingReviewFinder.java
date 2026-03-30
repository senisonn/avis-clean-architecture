package fr.esgi.soheil.kevin.application.port.in;

import fr.esgi.soheil.kevin.application.dto.Review;
import java.util.List;

public interface PendingReviewFinder {
    List<Review> getPendingReviews();
}