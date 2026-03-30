package fr.esgi.soheil.kevin.adapter.in.web;

import fr.esgi.soheil.kevin.application.dto.CreateReviewCommand;
import fr.esgi.soheil.kevin.application.dto.Review;
import fr.esgi.soheil.kevin.application.port.in.ReviewDeleter;
import fr.esgi.soheil.kevin.application.port.in.ReviewFinder;
import fr.esgi.soheil.kevin.application.port.in.ReviewSubmitter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewFinder reviewFinder;
    private final ReviewSubmitter reviewSubmitter;
    private final ReviewDeleter reviewDeleter;

    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<Review>> getByGame(@PathVariable Long gameId) {
        return ResponseEntity.ok(reviewFinder.getReviewsByGame(gameId));
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<Review>> getByPlayer(@PathVariable Long playerId) {
        return ResponseEntity.ok(reviewFinder.getReviewsByPlayer(playerId));
    }

    @PostMapping
    public ResponseEntity<Review> submit(@Valid @RequestBody CreateReviewCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewSubmitter.submitReview(command));
    }

    @DeleteMapping("/{reviewId}/player/{playerId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long reviewId,
            @PathVariable Long playerId) {
        reviewDeleter.deleteReview(reviewId, playerId);
        return ResponseEntity.noContent().build();
    }
}

