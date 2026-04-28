package fr.esgi.soheil.kevin.application.usecase.game;

import fr.esgi.soheil.kevin.application.dto.CreateGameCommand;
import fr.esgi.soheil.kevin.application.dto.Game;
import fr.esgi.soheil.kevin.application.port.in.GameUpdater;
import fr.esgi.soheil.kevin.application.port.out.GameRepository;
import fr.esgi.soheil.kevin.application.port.out.GenreRepository;
import fr.esgi.soheil.kevin.application.port.out.PublisherRepository;
import fr.esgi.soheil.kevin.domain.exception.GameNotFoundException;
import fr.esgi.soheil.kevin.domain.exception.GenreNotFoundException;
import fr.esgi.soheil.kevin.domain.exception.PublisherNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameUpdaterHandler implements GameUpdater {

    private final GameRepository     gameRepository;
    private final PublisherRepository publisherRepository;
    private final GenreRepository    genreRepository;

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

        if (command.publisherId() != null)
            game.setPublisher(publisherRepository.findById(command.publisherId())
                    .orElseThrow(() -> new PublisherNotFoundException(command.publisherId())));

        if (command.genreId() != null)
            game.setGenre(genreRepository.findById(command.genreId())
                    .orElseThrow(() -> new GenreNotFoundException(command.genreId())));

        return Game.fromDomain(gameRepository.save(game));
    }
}

