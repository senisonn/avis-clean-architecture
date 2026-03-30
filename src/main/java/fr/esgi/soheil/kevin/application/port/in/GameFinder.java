package fr.esgi.soheil.kevin.application.port.in;

import fr.esgi.soheil.kevin.application.dto.Game;
import java.util.List;

public interface GameFinder {
    List<Game> getAllGames();
    Game getGameById(Long id);
}