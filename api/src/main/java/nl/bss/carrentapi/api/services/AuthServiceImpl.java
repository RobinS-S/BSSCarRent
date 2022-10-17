package nl.bss.carrentapi.api.services;

import nl.bss.carrentapi.api.exceptions.UnauthenticatedException;
import nl.bss.carrentapi.api.models.User;
import nl.bss.carrentapi.api.repository.UserRepository;
import nl.bss.carrentapi.api.services.interfaces.AuthService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Gets the current user by the value of the Authorization header
     */
    @Override
    public User getCurrentUserByAuthHeader(String base64AuthHeaderValue) {
        if (base64AuthHeaderValue == null || base64AuthHeaderValue.length() <= 6) {
            throw new UnauthenticatedException();
        }

        String[] pair = new String(Base64.decodeBase64(base64AuthHeaderValue.substring(6))).split(":");
        String userName = pair[0];

        Optional<User> user = userRepository.findByEmail(userName);
        if (user.isEmpty()) {
            throw new UnauthenticatedException();
        }
        return user.get();
    }
}
