package fr.esgi.soheil.kevin.application.port.out;

import fr.esgi.soheil.kevin.domain.model.Genre;
import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    List<Genre>     findAll();
    Optional<Genre> findById(Long id);
    Genre           save(Genre genre);
    void            deleteById(Long id);
    boolean         existsById(Long id);
}