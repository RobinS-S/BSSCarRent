package nl.bss.carrentapi.api.services;

import lombok.AllArgsConstructor;
import nl.bss.carrentapi.api.dto.user.UserDto;
import nl.bss.carrentapi.api.dto.user.UserRegisterDto;
import nl.bss.carrentapi.api.models.Car;
import nl.bss.carrentapi.api.models.User;
import nl.bss.carrentapi.api.repository.CarRepository;
import nl.bss.carrentapi.api.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    /**
     * Creates a new user and hashes his password. NOTE: You must use this to create a new user, or the password won't get hashed. Still needs to be saved in order to persist.
     */
    public User createUser(String email, String password, String firstName, String infix, String lastName, String phoneInternationalCode, String phoneNumber, LocalDate birthDate) {
        User newUser = new User(email, new BCryptPasswordEncoder().encode(password), firstName, infix, lastName, phoneInternationalCode, phoneNumber, birthDate);
        return userRepository.save(newUser);
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUser(long id) {
        return userRepository.findById(id);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public boolean isEmailPresent(UserRegisterDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            return true;
        }
        return false;
    }

    public List<Car> listCarsByOwnerId(Long id) {
        return carRepository.findByOwnerId(id);
    }
}
