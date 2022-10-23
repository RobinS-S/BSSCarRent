package nl.bss.carrentapi.api.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String firstName;

    private String infix;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String phoneInternationalCode;

    @NotEmpty
    private String phoneNumber;

    @NotNull
    @Past
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
}
