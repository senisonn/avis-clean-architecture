package fr.esgi.soheil.kevin.adapter.out.persistence.repository;

import fr.esgi.soheil.kevin.adapter.out.persistence.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data interface — implemented automatically at runtime.
 * Never used directly by the application layer.
 * Only GamePersistenceAdapter calls this.
 */
public interface GameJpaRepository extends JpaRepository<GameEntity, Long> {

    boolean existsByName(String name);
}