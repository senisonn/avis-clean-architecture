package fr.esgi.soheil.kevin.adapter.out.persistence.mapper;

import fr.esgi.soheil.kevin.adapter.out.persistence.entity.GenreEntity;
import fr.esgi.soheil.kevin.domain.model.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {

    public Genre toDomain(GenreEntity e) {
        if (e == null) return null;
        Genre g = new Genre();
        g.setId(e.getId());
        g.setName(e.getName());
        return g;
    }

    public GenreEntity toEntity(Genre g) {
        if (g == null) return null;
        return GenreEntity.builder()
                .id(g.getId())
                .name(g.getName())
                .build();
    }
}

