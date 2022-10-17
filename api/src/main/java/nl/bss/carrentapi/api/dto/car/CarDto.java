package nl.bss.carrentapi.api.dto.car;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import nl.bss.carrentapi.api.enums.CarType;
import nl.bss.carrentapi.api.enums.CombustionFuelType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
public class CarDto {
    private long id;
    private long ownerId;

    @NotEmpty
    private String brand;

    @NotEmpty
    private String model;

    @NotEmpty
    private String color;

    @Min(value = 1)
    private long kilometersCurrent;

    @Min(value = 0)
    @NotNull
    private Double pricePerHour;

    @Min(value = 0)
    @NotNull
    private Double pricePerKilometer;

    @NotEmpty
    private String licensePlate;

    @Past
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate constructed;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate apkUntil;

    @NotNull
    private CarType carType;

    private CombustionFuelType fuelType;

    @NotNull
    private Double initialCost;

    @NotNull
    private Double lat;

    @NotNull
    private Double lng;
}
