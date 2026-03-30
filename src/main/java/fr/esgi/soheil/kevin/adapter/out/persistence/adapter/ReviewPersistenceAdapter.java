package fr.esgi.soheil.kevin.adapter.out.persistence.adapter;

import fr.esgi.soheil.kevin.adapter.out.persistence.mapper.ReviewMapper;
import fr.esgi.soheil.kevin.adapter.out.persistence.repository.ReviewJpaRepository;
import fr.esgi.soheil.kevin.application.port.out.ReviewRepository;
import fr.esgi.soheil.kevin.domain.model.Review;
import fr.esgi.soheil.kevin.domain.model.ReviewStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewPersistenceAdapter implements ReviewRepository {

    private final ReviewJpaRepository jpaRepository;
    private final ReviewMapper        mapper;

    @Override
    public List<Review> findByGameId(Long gameId) {
        return jpaRepository.findByGameEntityId(gameId)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Review> findByPlayerId(Long playerId) {
        return jpaRepository.findByPlayerId(playerId)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Review> findByStatus(ReviewStatus status) {
        fr.esgi.soheil.kevin.adapter.out.persistence.entity.ReviewStatus entityStatus =
                fr.esgi.soheil.kevin.adapter.out.persistence.entity.ReviewStatus
                        .valueOf(status.name());
        return jpaRepository.findByStatus(entityStatus)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Review> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Review save(Review review) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(review)));
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}