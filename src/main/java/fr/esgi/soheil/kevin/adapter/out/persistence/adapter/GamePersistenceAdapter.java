package fr.esgi.soheil.kevin.adapter.out.persistence.adapter;

import fr.esgi.soheil.kevin.adapter.out.persistence.mapper.GameMapper;
import fr.esgi.soheil.kevin.adapter.out.persistence.repository.GameJpaRepository;
import fr.esgi.soheil.kevin.application.port.out.GameRepository;
import fr.esgi.soheil.kevin.domain.model.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GamePersistenceAdapter implements GameRepository {

    private final GameJpaRepository jpaRepository;
    private final GameMapper        mapper;

    @Override
    public List<Game> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Game> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Game save(Game game) {
        return mapper.toDomain(
                jpaRepository.save(mapper.toEntity(game))
        );
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

