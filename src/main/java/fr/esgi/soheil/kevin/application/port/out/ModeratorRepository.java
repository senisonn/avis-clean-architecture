package fr.esgi.soheil.kevin.application.port.out;

import fr.esgi.soheil.kevin.domain.model.Moderator;
import java.util.Optional;

public interface ModeratorRepository {
    Optional<Moderator> findById(Long id);
    Optional<Moderator> findByEmail(String email);
    Moderator           save(Moderator moderator);
}