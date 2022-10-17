package nl.bss.carrentapi.api.services.interfaces;

import nl.bss.carrentapi.api.models.User;

import java.time.LocalDate;

public interface UserService {
    User createUser(String email, String password, String firstName, String infix, String lastName, String phoneInternationalCode, String phoneNumber, LocalDate birthDate);
}
