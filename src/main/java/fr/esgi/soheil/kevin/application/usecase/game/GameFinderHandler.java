package fr.esgi.soheil.kevin.application.usecase.game;

import fr.esgi.soheil.kevin.application.dto.Game;
import fr.esgi.soheil.kevin.application.port.in.GameFinder;
import fr.esgi.soheil.kevin.application.port.out.GameRepository;
import fr.esgi.soheil.kevin.domain.exception.GameNotFoundException;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class GameFinderHandler implements GameFinder {

    private final GameRepository gameRepository;

    @Override
    public List<Game> getAllGames() {
        return gameRepository.findAll()
                .stream()
                .map(Game::fromDomain)
                .toList();
    }

    @Override
    public Game getGameById(Long id) {
        return gameRepository.findById(id)
                .map(Game::fromDomain)
                .orElseThrow(() -> new GameNotFoundException(id));
    }
}

