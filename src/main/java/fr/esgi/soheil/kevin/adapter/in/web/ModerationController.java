package fr.esgi.soheil.kevin.adapter.in.web;

import fr.esgi.soheil.kevin.application.dto.Review;
import fr.esgi.soheil.kevin.application.port.in.PendingReviewFinder;
import fr.esgi.soheil.kevin.application.port.in.ReviewApprover;
import fr.esgi.soheil.kevin.application.port.in.ReviewRejector;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moderation")
@RequiredArgsConstructor
public class ModerationController {

    private final PendingReviewFinder pendingReviewFinder;
    private final ReviewApprover reviewApprover;
    private final ReviewRejector reviewRejector;

    @GetMapping("/pending")
    public ResponseEntity<List<Review>> getPending() {
        return ResponseEntity.ok(pendingReviewFinder.getPendingReviews());
    }

    @PatchMapping("/{reviewId}/approve/moderator/{moderatorId}")
    public ResponseEntity<Review> approve(
            @PathVariable Long reviewId,
            @PathVariable Long moderatorId) {
        return ResponseEntity.ok(reviewApprover.approveReview(reviewId, moderatorId));
    }

    @PatchMapping("/{reviewId}/reject/moderator/{moderatorId}")
    public ResponseEntity<Review> reject(
            @PathVariable Long reviewId,
            @PathVariable Long moderatorId) {
        return ResponseEntity.ok(reviewRejector.rejectReview(reviewId, moderatorId));
    }
}

