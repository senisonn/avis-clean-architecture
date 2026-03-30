package fr.esgi.soheil.kevin.adapter.out.persistence.repository;

import fr.esgi.soheil.kevin.adapter.out.persistence.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlayerJpaRepository extends JpaRepository<PlayerEntity, Long> {

    Optional<PlayerEntity> findByEmail(String email);

    Optional<PlayerEntity> findByUsername(String username);

    boolean existsByEmail(String email);
}