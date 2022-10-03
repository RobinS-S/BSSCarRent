package nl.bss.carrentapi.api.services.interfaces;

import nl.bss.carrentapi.api.models.entities.User;

import java.util.Optional;

public interface AuthService {
    public Optional<User> getCurrentUserByAuthHeader(String base64AuthHeaderValue);
}
