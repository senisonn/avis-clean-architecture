package fr.esgi.soheil.kevin.application.usecase.game;

import fr.esgi.soheil.kevin.application.dto.CreateGameCommand;
import fr.esgi.soheil.kevin.application.dto.Game;
import fr.esgi.soheil.kevin.application.port.in.GameCreator;
import fr.esgi.soheil.kevin.application.port.out.GameRepository;
import fr.esgi.soheil.kevin.domain.model.AgeRating;
import fr.esgi.soheil.kevin.domain.model.Genre;
import fr.esgi.soheil.kevin.domain.model.Publisher;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameCreatorHandler implements GameCreator {

    private final GameRepository gameRepository;

    @Override
    public Game addGame(CreateGameCommand command) {
        fr.esgi.soheil.kevin.domain.model.Game game =
                new fr.esgi.soheil.kevin.domain.model.Game();
        game.setName(command.name());
        game.setDescription(command.description());
        game.setPrice(command.price());
        game.setReleaseDate(command.releaseDate());
        game.setImageUrl(command.imageUrl());

        if (command.genreId() != null) {
            Genre g = new Genre();
            g.setId(command.genreId());
            game.setGenre(g);
        }
        if (command.publisherId() != null) {
            Publisher p = new Publisher();
            p.setId(command.publisherId());
            game.setPublisher(p);
        }
        if (command.ageRatingId() != null) {
            AgeRating a = new AgeRating();
            a.setId(command.ageRatingId());
            game.setAgeRating(a);
        }

        return Game.fromDomain(gameRepository.save(game));
    }
}

