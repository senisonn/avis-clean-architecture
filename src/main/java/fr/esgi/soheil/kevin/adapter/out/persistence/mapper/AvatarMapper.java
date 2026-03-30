package fr.esgi.soheil.kevin.adapter.out.persistence.mapper;

import fr.esgi.soheil.kevin.adapter.out.persistence.entity.AvatarEntity;
import fr.esgi.soheil.kevin.domain.model.Avatar;
import org.springframework.stereotype.Component;

@Component
public class AvatarMapper {

    public Avatar toDomain(AvatarEntity e) {
        if (e == null) return null;
        Avatar a = new Avatar();
        a.setId(e.getId());
        a.setName(e.getName());
        return a;
    }

    public AvatarEntity toEntity(Avatar a) {
        if (a == null) return null;
        return AvatarEntity.builder()
                .id(a.getId())
                .name(a.getName())
                .build();
    }
}

