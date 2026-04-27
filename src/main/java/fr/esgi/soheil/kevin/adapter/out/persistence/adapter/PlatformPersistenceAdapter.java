package fr.esgi.soheil.kevin.adapter.out.persistence.adapter;

import fr.esgi.soheil.kevin.adapter.out.persistence.mapper.PlatformMapper;
import fr.esgi.soheil.kevin.adapter.out.persistence.repository.PlatformJpaRepository;
import fr.esgi.soheil.kevin.application.port.out.PlatformRepository;
import fr.esgi.soheil.kevin.domain.model.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlatformPersistenceAdapter implements PlatformRepository {

    private final PlatformJpaRepository jpaRepository;
    private final PlatformMapper        mapper;

    @Override
    public List<Platform> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Platform> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Platform save(Platform platform) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(platform)));
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}