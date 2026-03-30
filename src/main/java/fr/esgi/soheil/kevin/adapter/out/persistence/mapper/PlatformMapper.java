package fr.esgi.soheil.kevin.adapter.out.persistence.mapper;

import fr.esgi.soheil.kevin.adapter.out.persistence.entity.PlatformEntity;
import fr.esgi.soheil.kevin.domain.model.Platform;
import org.springframework.stereotype.Component;

@Component
public class PlatformMapper {

    public Platform toDomain(PlatformEntity e) {
        if (e == null) return null;
        Platform p = new Platform();
        p.setId(e.getId());
        p.setName(e.getName());
        p.setReleaseDate(e.getReleaseDate());
        return p;
    }

    public PlatformEntity toEntity(Platform p) {
        if (p == null) return null;
        return PlatformEntity.builder()
                .id(p.getId())
                .name(p.getName())
                .releaseDate(p.getReleaseDate())
                .build();
    }
}

