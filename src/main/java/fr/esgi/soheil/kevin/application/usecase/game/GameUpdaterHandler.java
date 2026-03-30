package fr.esgi.soheil.kevin.application.usecase.game;

import fr.esgi.soheil.kevin.application.dto.CreateGameCommand;
import fr.esgi.soheil.kevin.application.dto.Game;
import fr.esgi.soheil.kevin.application.port.in.GameUpdater;
import fr.esgi.soheil.kevin.application.port.out.GameRepository;
import fr.esgi.soheil.kevin.domain.exception.GameNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameUpdaterHandler implements GameUpdater {

    private final GameRepository gameRepository;

    @Override
    public Game updateGame(Long id, CreateGameCommand command) {
        fr.esgi.soheil.kevin.domain.model.Game game =
                gameRepository.findById(id)
                        .orElseThrow(() -> new GameNotFoundException(id));

        game.setName(command.name());
        game.setDescription(command.description());
        game.setPrice(command.price());
        game.setReleaseDate(command.releaseDate());
        game.setImageUrl(command.imageUrl());

        return Game.fromDomain(gameRepository.save(game));
    }
}

