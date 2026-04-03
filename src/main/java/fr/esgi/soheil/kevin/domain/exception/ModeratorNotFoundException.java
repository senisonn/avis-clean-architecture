package fr.esgi.soheil.kevin.domain.exception;

public class ModeratorNotFoundException extends RuntimeException {

    public ModeratorNotFoundException(Long id) {
        super("Moderator not found with id: " + id);
    }

    public ModeratorNotFoundException(String message) {
        super(message);
    }
}