package nl.bss.carrentapi.api.services.interfaces;

import nl.bss.carrentapi.api.models.User;

public interface AuthService {
    User getCurrentUserByAuthHeader(String base64AuthHeaderValue);
}
