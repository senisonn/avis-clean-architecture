package fr.esgi.soheil.kevin.application.usecase.genre;

import fr.esgi.soheil.kevin.application.port.in.GenreManager;
import fr.esgi.soheil.kevin.application.port.out.GenreRepository;
import fr.esgi.soheil.kevin.domain.model.Genre;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class GenreManagerHandler implements GenreManager {

    private final GenreRepository genreRepository;

    @Override
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    @Override
    public Genre add(String name) {
        Genre genre = new Genre();
        genre.setName(name);
        return genreRepository.save(genre);
    }

    @Override
    public void delete(Long id) {
        if (!genreRepository.existsById(id))
            throw new IllegalArgumentException("Genre not found: " + id);
        genreRepository.deleteById(id);
    }
}