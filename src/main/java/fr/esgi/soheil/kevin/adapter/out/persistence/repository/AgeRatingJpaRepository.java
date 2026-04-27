package fr.esgi.soheil.kevin.adapter.out.persistence.repository;

import fr.esgi.soheil.kevin.adapter.out.persistence.entity.AgeRatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgeRatingJpaRepository extends JpaRepository<AgeRatingEntity, Long> {
}
