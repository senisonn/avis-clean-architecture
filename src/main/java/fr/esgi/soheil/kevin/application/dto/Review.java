package fr.esgi.soheil.kevin.application.dto;

import fr.esgi.soheil.kevin.domain.model.ReviewStatus;
import java.time.LocalDateTime;

public record Review(
        Long          id,
        String        content,
        Float         rating,
        LocalDateTime submittedAt,
        ReviewStatus  status,
        String        playerUsername,
        String        gameName
) {
    public static Review fromDomain(fr.esgi.soheil.kevin.domain.model.Review r) {
        return new Review(
                r.getId(),
                r.getContent(),
                r.getRating(),
                r.getSubmittedAt(),
                r.getStatus(),
                r.getPlayer() != null ? r.getPlayer().getUsername() : null,
                r.getGame()   != null ? r.getGame().getName()       : null
        );
    }
}

