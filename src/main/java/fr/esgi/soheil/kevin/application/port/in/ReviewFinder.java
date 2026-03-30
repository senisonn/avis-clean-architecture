package fr.esgi.soheil.kevin.application.port.in;

import fr.esgi.soheil.kevin.application.dto.Review;
import java.util.List;

public interface ReviewFinder {
    List<Review> getReviewsByGame(Long gameId);
    List<Review> getReviewsByPlayer(Long playerId);
}