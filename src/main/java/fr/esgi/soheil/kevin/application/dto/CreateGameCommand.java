package fr.esgi.soheil.kevin.application.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * Sent by the controller when creating or updating a game.
 * Validated at the HTTP boundary with @Valid.
 */
public record CreateGameCommand(

        @NotBlank
        String    name,

        String    description,

        String    imageUrl,

        @PositiveOrZero
        Float     price,

        LocalDate releaseDate,

        Long      genreId,

        Long      publisherId,

        Long      ageRatingId
) {}