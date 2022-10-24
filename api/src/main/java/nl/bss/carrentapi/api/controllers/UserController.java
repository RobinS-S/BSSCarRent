package nl.bss.carrentapi.api.controllers;

import lombok.AllArgsConstructor;
import nl.bss.carrentapi.api.dto.car.CarDto;
import nl.bss.carrentapi.api.dto.user.UserDto;
import nl.bss.carrentapi.api.dto.user.UserRegisterDto;
import nl.bss.carrentapi.api.dto.user.UserUpdateDto;
import nl.bss.carrentapi.api.exceptions.ConflictException;
import nl.bss.carrentapi.api.exceptions.NotAllowedException;
import nl.bss.carrentapi.api.exceptions.NotFoundException;
import nl.bss.carrentapi.api.mappers.DtoMapper;
import nl.bss.carrentapi.api.models.Car;
import nl.bss.carrentapi.api.models.User;
import nl.bss.carrentapi.api.repository.CarRepository;
import nl.bss.carrentapi.api.repository.UserRepository;
import nl.bss.carrentapi.api.services.interfaces.AuthService;
import nl.bss.carrentapi.api.services.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Validated
@AllArgsConstructor
public class UserController {
    private final DtoMapper dtoMapper;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<UserDto>> index() {
        List<User> users = userRepository.findAll();

        return ResponseEntity.ok(users.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    /**
     * Gets User by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("That user was not found."));
        return ResponseEntity.ok(dtoMapper.convertToDto(user));
    }

    /**
     * Gets Cars that are owned by given User.
     */
    @GetMapping("/{id}/cars")
    public ResponseEntity<List<CarDto>> getUserCars(@PathVariable Long id) {
        List<Car> cars = carRepository.findByOwnerId(id);
        return ResponseEntity.ok(cars.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    /**
     * Updates the given User.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@RequestHeader(name = "Authorization", required = false) String authHeader, @PathVariable Long id, @Valid @RequestBody UserUpdateDto updateDto) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);
        if (!id.equals(user.getId())) {
            throw new NotAllowedException("You're not allowed to update this User, because you're logged in under another User.");
        }

        modelMapper.map(updateDto, user);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(user));
    }

    /**
     * Fails if user isn't logged in correctly, returns active User
     */
    @PostMapping("/profile")
    public ResponseEntity<UserDto> login(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);
        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(user));
    }

    /**
     * Creates User
     */
    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserRegisterDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new ConflictException("This email is already used for an account. Please log in.");
        }

        User user = userService.createUser(userDto.getEmail(), userDto.getPassword(), userDto.getFirstName(), userDto.getInfix(), userDto.getLastName(), userDto.getPhoneInternationalCode(), userDto.getPhoneNumber(), userDto.getBirthDate());

        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.convertToDto(user));
    }
}
