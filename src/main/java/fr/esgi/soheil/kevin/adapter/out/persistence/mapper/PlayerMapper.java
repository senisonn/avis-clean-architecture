package fr.esgi.soheil.kevin.adapter.out.persistence.mapper;

import fr.esgi.soheil.kevin.adapter.out.persistence.entity.PlayerEntity;
import fr.esgi.soheil.kevin.domain.model.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerMapper {

    private final AvatarMapper avatarMapper;

    
    public Player toDomain(PlayerEntity e) {
        if (e == null) return null;
        Player p = new Player();
        p.setId(e.getId());
        p.setUsername(e.getUsername());
        p.setEmail(e.getEmail());
        p.setPassword(e.getPassword());
        p.setBirthDate(e.getBirthDate());
        p.setAvatar(avatarMapper.toDomain(e.getAvatar()));
        return p;
    }

    
    public PlayerEntity toEntity(Player p) {
        if (p == null) return null;
        return PlayerEntity.builder()
                .id(p.getId())
                .username(p.getUsername())
                .email(p.getEmail())
                .password(p.getPassword())
                .birthDate(p.getBirthDate())
                .avatar(avatarMapper.toEntity(p.getAvatar()))
                .build();
    }
}

