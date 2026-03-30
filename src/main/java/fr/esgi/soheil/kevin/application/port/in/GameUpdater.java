package fr.esgi.soheil.kevin.application.port.in;

import fr.esgi.soheil.kevin.application.dto.CreateGameCommand;
import fr.esgi.soheil.kevin.application.dto.Game;

public interface GameUpdater {
    Game updateGame(Long id, CreateGameCommand command);
}