package fr.esgi.soheil.kevin.domain.exception;

public class InvalidRatingException extends RuntimeException {

    public InvalidRatingException(Float rating) {
        super("Rating must be between 0.0 and 5.0, got: " + rating);
    }
}