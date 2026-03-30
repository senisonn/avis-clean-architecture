package fr.esgi.soheil.kevin.application.usecase.review;

import fr.esgi.soheil.kevin.application.dto.CreateReviewCommand;
import fr.esgi.soheil.kevin.application.dto.Review;          // ← dto
import fr.esgi.soheil.kevin.application.port.in.ReviewSubmitter;
import fr.esgi.soheil.kevin.application.port.out.GameRepository;
import fr.esgi.soheil.kevin.application.port.out.PlayerRepository;
import fr.esgi.soheil.kevin.application.port.out.ReviewRepository;
import fr.esgi.soheil.kevin.domain.exception.GameNotFoundException;
import fr.esgi.soheil.kevin.domain.exception.ReviewNotFoundException;
// ← pas d'import pour domain.model.Review — on utilise le nom complet dans le code
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewSubmitterHandler implements ReviewSubmitter {

    private final ReviewRepository reviewRepository;
    private final GameRepository   gameRepository;
    private final PlayerRepository playerRepository;

    @Override
    public Review submitReview(CreateReviewCommand command) {
        var game = gameRepository.findById(command.gameId())
                .orElseThrow(() -> new GameNotFoundException(command.gameId()));
        var player = playerRepository.findById(command.playerId())
                .orElseThrow(() -> new ReviewNotFoundException(command.playerId()));

        // nom complet qualifié pour lever l'ambiguïté avec le dto Review
        fr.esgi.soheil.kevin.domain.model.Review review =
                new fr.esgi.soheil.kevin.domain.model.Review();
        review.setContent(command.content());
        review.setRating(command.rating());
        review.setGame(game);
        review.setPlayer(player);

        review.publish();

        return Review.fromDomain(reviewRepository.save(review));
    }
}