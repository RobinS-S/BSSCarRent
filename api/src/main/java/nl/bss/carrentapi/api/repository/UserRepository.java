package nl.bss.carrentapi.api.repository;

import nl.bss.carrentapi.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a User by its e-mail address
     *
     * @param email
     * @return
     */
    Optional<User> findByEmail(String email);
}