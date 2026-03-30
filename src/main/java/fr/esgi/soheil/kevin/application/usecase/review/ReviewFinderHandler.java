package fr.esgi.soheil.kevin.application.usecase.review;

import fr.esgi.soheil.kevin.application.dto.Review;
import fr.esgi.soheil.kevin.application.port.in.ReviewFinder;
import fr.esgi.soheil.kevin.application.port.out.ReviewRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class ReviewFinderHandler implements ReviewFinder {

    private final ReviewRepository reviewRepository;

    @Override
    public List<Review> getReviewsByGame(Long gameId) {
        return reviewRepository.findByGameId(gameId)
                .stream().map(Review::fromDomain).toList();
    }

    @Override
    public List<Review> getReviewsByPlayer(Long playerId) {
        return reviewRepository.findByPlayerId(playerId)
                .stream().map(Review::fromDomain).toList();
    }
}

