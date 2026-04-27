package fr.esgi.soheil.kevin.adapter.out.persistence.adapter;

import fr.esgi.soheil.kevin.adapter.out.persistence.mapper.GenreMapper;
import fr.esgi.soheil.kevin.adapter.out.persistence.repository.GenreJpaRepository;
import fr.esgi.soheil.kevin.application.port.out.GenreRepository;
import fr.esgi.soheil.kevin.domain.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenrePersistenceAdapter implements GenreRepository {

    private final GenreJpaRepository jpaRepository;
    private final GenreMapper        mapper;

    @Override
    public List<Genre> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Genre> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Genre save(Genre genre) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(genre)));
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