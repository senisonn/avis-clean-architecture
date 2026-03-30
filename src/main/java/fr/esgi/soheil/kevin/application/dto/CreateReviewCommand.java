package fr.esgi.soheil.kevin.application.dto;

import jakarta.validation.constraints.*;

public record CreateReviewCommand(

        @NotNull
        Long   gameId,

        @NotNull
        Long   playerId,

        @NotBlank
        String content,

        @NotNull
        @DecimalMin("0.0")
        @DecimalMax("5.0")
        Float  rating
) {}