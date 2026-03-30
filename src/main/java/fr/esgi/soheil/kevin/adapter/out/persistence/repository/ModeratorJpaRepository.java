package fr.esgi.soheil.kevin.adapter.out.persistence.repository;

import fr.esgi.soheil.kevin.adapter.out.persistence.entity.ModeratorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ModeratorJpaRepository extends JpaRepository<ModeratorEntity, Long> {

    Optional<ModeratorEntity> findByEmail(String email);
}