package nl.bss.carrentapi.api.services;

import nl.bss.carrentapi.api.models.entities.User;
import nl.bss.carrentapi.api.services.interfaces.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserServiceImpl implements UserService {

    public UserServiceImpl() {
    }

    /**
     * Creates a new user and hashes his password. NOTE: You must use this to create a new user, or the password won't get hashed. Still needs to be saved in order to persist.
     */
    @Override
    public User createUser(String email, String password, String firstName, String infix, String lastName, String phoneInternationalCode, String phoneNumber, LocalDate birthDate) {
        return new User(email, new BCryptPasswordEncoder().encode(password), firstName, infix, lastName, phoneInternationalCode, phoneNumber, birthDate);
    }
}