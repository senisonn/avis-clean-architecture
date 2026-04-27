package fr.esgi.soheil.kevin.application.usecase.platform;

import fr.esgi.soheil.kevin.application.port.in.PlatformManager;
import fr.esgi.soheil.kevin.application.port.out.PlatformRepository;
import fr.esgi.soheil.kevin.domain.model.Platform;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class PlatformManagerHandler implements PlatformManager {

    private final PlatformRepository platformRepository;

    @Override
    public List<Platform> getAll() {
        return platformRepository.findAll();
    }

    @Override
    public Platform add(String name, LocalDate releaseDate) {
        Platform platform = new Platform();
        platform.setName(name);
        platform.setReleaseDate(releaseDate);
        return platformRepository.save(platform);
    }

    @Override
    public void delete(Long id) {
        if (!platformRepository.existsById(id))
            throw new IllegalArgumentException("Platform not found: " + id);
        platformRepository.deleteById(id);
    }
}