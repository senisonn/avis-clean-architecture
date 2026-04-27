package fr.esgi.soheil.kevin.adapter.out.persistence.adapter;

import fr.esgi.soheil.kevin.adapter.out.persistence.mapper.PublisherMapper;
import fr.esgi.soheil.kevin.adapter.out.persistence.repository.PublisherJpaRepository;
import fr.esgi.soheil.kevin.application.port.out.PublisherRepository;
import fr.esgi.soheil.kevin.domain.model.Publisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PublisherPersistenceAdapter implements PublisherRepository {

    private final PublisherJpaRepository jpaRepository;
    private final PublisherMapper        mapper;

    @Override
    public List<Publisher> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Publisher> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Publisher save(Publisher publisher) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(publisher)));
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