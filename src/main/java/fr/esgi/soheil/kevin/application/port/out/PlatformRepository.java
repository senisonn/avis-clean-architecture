package fr.esgi.soheil.kevin.application.port.out;

import fr.esgi.soheil.kevin.domain.model.Platform;
import java.util.List;
import java.util.Optional;

public interface PlatformRepository {
    List<Platform>     findAll();
    Optional<Platform> findById(Long id);
    Platform           save(Platform platform);
    void               deleteById(Long id);
    boolean            existsById(Long id);
}