package fr.esgi.soheil.kevin.adapter.out.persistence.repository;

import fr.esgi.soheil.kevin.adapter.out.persistence.entity.ReviewEntity;
import fr.esgi.soheil.kevin.adapter.out.persistence.entity.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, Long> {

    List<ReviewEntity> findByGameEntityId(Long gameId);

    List<ReviewEntity> findByPlayerId(Long playerId);

    List<ReviewEntity> findByStatus(ReviewStatus status);
}