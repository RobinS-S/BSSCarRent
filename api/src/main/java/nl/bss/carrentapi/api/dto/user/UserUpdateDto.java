package nl.bss.carrentapi.api.dto.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserUpdateDto {
    @NotEmpty
    private String firstName;

    private String infix;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String phoneInternationalCode;

    @NotEmpty
    private String phoneNumber;
}
