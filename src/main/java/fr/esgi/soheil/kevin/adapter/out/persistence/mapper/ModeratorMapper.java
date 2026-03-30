package fr.esgi.soheil.kevin.adapter.out.persistence.mapper;

import fr.esgi.soheil.kevin.adapter.out.persistence.entity.ModeratorEntity;
import fr.esgi.soheil.kevin.domain.model.Moderator;
import org.springframework.stereotype.Component;

@Component
public class ModeratorMapper {

    // Entity → Domain
    public Moderator toDomain(ModeratorEntity e) {
        if (e == null) return null;
        Moderator m = new Moderator();
        m.setId(e.getId());
        m.setUsername(e.getUsername());
        m.setEmail(e.getEmail());
        m.setPassword(e.getPassword());
        m.setPhoneNumber(e.getPhoneNumber());
        return m;
    }

    // Domain → Entity
    public ModeratorEntity toEntity(Moderator m) {
        if (m == null) return null;
        return ModeratorEntity.builder()
                .id(m.getId())
                .username(m.getUsername())
                .email(m.getEmail())
                .password(m.getPassword())
                .phoneNumber(m.getPhoneNumber())
                .build();
    }
}

