package nl.bss.carrentapi.api.dto.car;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class CarUpdateDto {
    @NotEmpty
    private String brand;

    @NotEmpty
    private String model;

    @NotEmpty
    private String color;

    @Min(value = 1)
    private int kilometersCurrent;

    @Min(value = 0)
    private Double pricePerKilometer;

    @NotEmpty
    private String licensePlate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate apkUntil;

    @NotNull
    private Double initialCost;
}
