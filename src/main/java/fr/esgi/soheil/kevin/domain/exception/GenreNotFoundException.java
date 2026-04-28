package fr.esgi.soheil.kevin.domain.exception;

public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException(Long id) {
        super("Genre not found with id: " + id);
    }

    public GenreNotFoundException(String message) {
        super(message);
    }
}
