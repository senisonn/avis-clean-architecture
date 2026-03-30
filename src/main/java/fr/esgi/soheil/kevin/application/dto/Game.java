package fr.esgi.soheil.kevin.application.dto;

import java.time.LocalDate;

public record Game(
        Long      id,
        String    name,
        String    description,
        String    imageUrl,
        Float     price,
        LocalDate releaseDate,
        String    genre,
        String    publisher,
        String    ageRating
) {
    public static Game fromDomain(fr.esgi.soheil.kevin.domain.model.Game g) {
        return new Game(
                g.getId(),
                g.getName(),
                g.getDescription(),
                g.getImageUrl(),
                g.getPrice(),
                g.getReleaseDate(),
                g.getGenre()     != null ? g.getGenre().getName()      : null,
                g.getPublisher() != null ? g.getPublisher().getName()  : null,
                g.getAgeRating() != null ? g.getAgeRating().getLabel() : null
        );
    }
}