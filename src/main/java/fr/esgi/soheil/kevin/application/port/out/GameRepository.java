package fr.esgi.soheil.kevin.application.port.out;

import fr.esgi.soheil.kevin.domain.model.Game;
import java.util.List;
import java.util.Optional;

public interface GameRepository {
    List<Game>     findAll();
    Optional<Game> findById(Long id);
    Game           save(Game game);
    void           deleteById(Long id);
    boolean        existsById(Long id);
}