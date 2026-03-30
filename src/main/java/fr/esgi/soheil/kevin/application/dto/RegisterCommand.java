package fr.esgi.soheil.kevin.application.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record RegisterCommand(

        @NotBlank
        String    username,

        @Email
        @NotBlank
        String    email,

        @NotBlank
        @Size(min = 8)
        String    password,

        LocalDate birthDate
) {}