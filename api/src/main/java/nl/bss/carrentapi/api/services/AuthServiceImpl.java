package nl.bss.carrentapi.api.services;

import lombok.AllArgsConstructor;
import nl.bss.carrentapi.api.exceptions.UnauthenticatedException;
import nl.bss.carrentapi.api.models.User;
import nl.bss.carrentapi.api.repository.UserRepository;
import nl.bss.carrentapi.api.services.interfaces.AuthService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

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
        if(pair.length == 1) {
            throw new UnauthenticatedException("You must provide a password.");
        }
        String password = pair[1];

        User user = userRepository.findByEmail(userName).orElseThrow(() -> new UnauthenticatedException("User not found."));
        if(!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            throw new UnauthenticatedException("The password you provided is incorrect.");
        }

        return user;
    }
}
