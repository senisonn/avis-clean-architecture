package fr.esgi.soheil.kevin.adapter.out.persistence.adapter;

import fr.esgi.soheil.kevin.adapter.out.persistence.mapper.PlayerMapper;
import fr.esgi.soheil.kevin.adapter.out.persistence.repository.PlayerJpaRepository;
import fr.esgi.soheil.kevin.application.port.out.PlayerRepository;
import fr.esgi.soheil.kevin.domain.model.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlayerPersistenceAdapter implements PlayerRepository {

    private final PlayerJpaRepository jpaRepository;
    private final PlayerMapper        mapper;

    @Override
    public Optional<Player> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Player> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Player> findByUsername(String username) {
        return jpaRepository.findByUsername(username)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public Player save(Player player) {
        return mapper.toDomain(
                jpaRepository.save(mapper.toEntity(player))
        );
    }
}

