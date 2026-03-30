package fr.esgi.soheil.kevin.adapter.out.persistence.adapter;

import fr.esgi.soheil.kevin.adapter.out.persistence.mapper.ModeratorMapper;
import fr.esgi.soheil.kevin.adapter.out.persistence.repository.ModeratorJpaRepository;
import fr.esgi.soheil.kevin.application.port.out.ModeratorRepository;
import fr.esgi.soheil.kevin.domain.model.Moderator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ModeratorPersistenceAdapter implements ModeratorRepository {

    private final ModeratorJpaRepository jpaRepository;
    private final ModeratorMapper        mapper;

    @Override
    public Optional<Moderator> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Moderator> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public Moderator save(Moderator moderator) {
        return mapper.toDomain(
                jpaRepository.save(mapper.toEntity(moderator))
        );
    }
}

