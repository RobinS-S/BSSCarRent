package nl.bss.carrentapi.api.services;

import nl.bss.carrentapi.api.models.entities.User;
import nl.bss.carrentapi.api.repository.UserRepository;
import nl.bss.carrentapi.api.services.interfaces.AuthService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** Gets the current user by the value of the Authorization header*/
    @Override
    public Optional<User> getCurrentUserByAuthHeader(String base64AuthHeaderValue) {
        if(base64AuthHeaderValue.length() <= 6) {
            return Optional.empty();
        }

        String pair = new String(Base64.decodeBase64(base64AuthHeaderValue.substring(6)));
        String userName = pair.split(":")[0];
        return userRepository.findByEmail(userName);
    }
}
