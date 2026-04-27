package fr.esgi.soheil.kevin.application.port.in;

import fr.esgi.soheil.kevin.domain.model.Platform;
import java.time.LocalDate;
import java.util.List;

public interface PlatformManager {
    List<Platform> getAll();
    Platform       add(String name, LocalDate releaseDate);
    void           delete(Long id);
}