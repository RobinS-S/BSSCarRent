package nl.bss.carrentapi.api.controllers;

import nl.bss.carrentapi.api.mappers.DtoMapper;
import nl.bss.carrentapi.api.models.dto.user.UserRegisterDto;
import nl.bss.carrentapi.api.models.dto.car.CarDto;
import nl.bss.carrentapi.api.models.dto.user.UserDto;
import nl.bss.carrentapi.api.models.dto.user.UserUpdateDto;
import nl.bss.carrentapi.api.models.entities.Car;
import nl.bss.carrentapi.api.models.entities.User;
import nl.bss.carrentapi.api.repository.CarRepository;
import nl.bss.carrentapi.api.repository.UserRepository;
import nl.bss.carrentapi.api.services.interfaces.AuthService;
import nl.bss.carrentapi.api.services.interfaces.UserService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
    private final DtoMapper dtoMapper;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final AuthService authService;

    public UserController(DtoMapper dtoMapper, ModelMapper modelMapper, UserService userService, UserRepository userRepository, CarRepository carRepository, AuthService authService) {
        this.dtoMapper = dtoMapper;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> index() {
        List<User> users = userRepository.findAll();

        return ResponseEntity.ok(users.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDto userDto = dtoMapper.convertToDto(user.get());

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{id}/cars")
    public ResponseEntity<List<CarDto>> getUserCars(@PathVariable Long id) {
        List<Car> cars = carRepository.findByOwnerId(id);
        return ResponseEntity.ok(cars.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@RequestHeader("Authorization") String authHeader, @PathVariable Long id, @Valid @RequestBody UserUpdateDto updateDto) {
        Optional<User> foundUser = authService.getCurrentUserByAuthHeader(authHeader);
        if (foundUser.isEmpty() || !id.equals(foundUser.get().getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        User user = foundUser.get();
        modelMapper.map(updateDto, user);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(user));
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserRegisterDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        User user = userService.createUser(userDto.getEmail(), userDto.getPassword(), userDto.getFirstName(), userDto.getInfix(), userDto.getLastName(), userDto.getPhoneInternationalCode(), userDto.getPhoneNumber(), userDto.getBirthDate());
        user = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.convertToDto(user));
    }
}
