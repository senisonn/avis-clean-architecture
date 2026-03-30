package fr.esgi.soheil.kevin.application.port.in;

public interface ReviewDeleter {
    void deleteReview(Long reviewId, Long playerId);
}