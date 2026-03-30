package fr.esgi.soheil.kevin.application.port.in;

import fr.esgi.soheil.kevin.application.dto.AuthResponse;
import fr.esgi.soheil.kevin.application.dto.RegisterCommand;

public interface PlayerRegistrar {
    AuthResponse register(RegisterCommand command);
}