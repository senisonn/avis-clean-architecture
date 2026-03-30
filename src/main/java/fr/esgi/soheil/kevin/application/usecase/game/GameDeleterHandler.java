package fr.esgi.soheil.kevin.application.usecase.game;

import fr.esgi.soheil.kevin.application.port.in.GameDeleter;
import fr.esgi.soheil.kevin.application.port.out.GameRepository;
import fr.esgi.soheil.kevin.domain.exception.GameNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameDeleterHandler implements GameDeleter {

    private final GameRepository gameRepository;

    @Override
    public void deleteGame(Long id) {
        if (!gameRepository.existsById(id))
            throw new GameNotFoundException(id);
        gameRepository.deleteById(id);
    }
}