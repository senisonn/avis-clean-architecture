package fr.esgi.soheil.kevin.application.port.in;

import fr.esgi.soheil.kevin.domain.model.Genre;
import java.util.List;

public interface GenreManager {
    List<Genre> getAll();
    Genre       add(String name);
    void        delete(Long id);
}