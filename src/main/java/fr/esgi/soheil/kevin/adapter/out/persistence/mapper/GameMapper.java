package fr.esgi.soheil.kevin.adapter.out.persistence.mapper;

import fr.esgi.soheil.kevin.adapter.out.persistence.entity.GameEntity;
import fr.esgi.soheil.kevin.domain.model.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GameMapper {

    private final GenreMapper    genreMapper;
    private final PublisherMapper publisherMapper;
    private final AgeRatingMapper ageRatingMapper;
    private final PlatformMapper  platformMapper;

    public Game toDomain(GameEntity e) {
        if (e == null) return null;
        Game g = new Game();
        g.setId(e.getId());
        g.setName(e.getName());
        g.setDescription(e.getDescription());
        g.setImageUrl(e.getImageUrl());
        g.setPrice(e.getPrice());
        g.setReleaseDate(e.getReleaseDate());
        g.setGenre(genreMapper.toDomain(e.getGenre()));
        g.setPublisher(publisherMapper.toDomain(e.getPublisher()));
        g.setAgeRating(ageRatingMapper.toDomain(e.getAgeRatingEntity()));
        g.setPlatforms(
                e.getPlatforms() == null ? List.of()
                        : e.getPlatforms().stream().map(platformMapper::toDomain).toList()
        );
        return g;
    }

    public GameEntity toEntity(Game g) {
        if (g == null) return null;
        return GameEntity.builder()
                .id(g.getId())
                .name(g.getName())
                .description(g.getDescription())
                .imageUrl(g.getImageUrl())
                .price(g.getPrice())
                .releaseDate(g.getReleaseDate())
                .genre(genreMapper.toEntity(g.getGenre()))
                .publisher(publisherMapper.toEntity(g.getPublisher()))
                // builder field nommé ageRatingEntity dans GameEntity
                .ageRatingEntity(ageRatingMapper.toEntity(g.getAgeRating()))
                .platforms(
                        g.getPlatforms() == null ? List.of()
                                : g.getPlatforms().stream().map(platformMapper::toEntity).toList()
                )
                .build();
    }
}

