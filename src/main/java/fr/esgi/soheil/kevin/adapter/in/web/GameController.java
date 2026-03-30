package fr.esgi.soheil.kevin.adapter.in.web;

import fr.esgi.soheil.kevin.application.dto.CreateGameCommand;
import fr.esgi.soheil.kevin.application.dto.Game;
import fr.esgi.soheil.kevin.application.port.in.GameCreator;
import fr.esgi.soheil.kevin.application.port.in.GameDeleter;
import fr.esgi.soheil.kevin.application.port.in.GameFinder;
import fr.esgi.soheil.kevin.application.port.in.GameUpdater;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final GameFinder  gameFinder;
    private final GameCreator gameCreator;
    private final GameUpdater gameUpdater;
    private final GameDeleter gameDeleter;

    @GetMapping
    public ResponseEntity<List<Game>> getAll() {
        return ResponseEntity.ok(gameFinder.getAllGames());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getById(@PathVariable Long id) {
        return ResponseEntity.ok(gameFinder.getGameById(id));
    }

    @PostMapping
    public ResponseEntity<Game> create(@Valid @RequestBody CreateGameCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(gameCreator.addGame(command));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Game> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateGameCommand command) {
        return ResponseEntity.ok(gameUpdater.updateGame(id, command));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gameDeleter.deleteGame(id);
        return ResponseEntity.noContent().build();
    }
}