package fr.esgi.soheil.kevin.domain.exception;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(Long id) {
        super("Game not found with id: " + id);
    }

    public GameNotFoundException(String message) {
        super(message);
    }
}