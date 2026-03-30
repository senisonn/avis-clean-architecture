package fr.esgi.soheil.kevin.application.port.out;

import fr.esgi.soheil.kevin.domain.model.Player;
import java.util.Optional;

public interface PlayerRepository {
    Optional<Player> findById(Long id);
    Optional<Player> findByEmail(String email);
    Optional<Player> findByUsername(String username);
    boolean          existsByEmail(String email);
    Player           save(Player player);
}