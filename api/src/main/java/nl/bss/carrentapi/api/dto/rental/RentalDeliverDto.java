package nl.bss.carrentapi.api.dto.rental;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RentalDeliverDto {
    @Min(0)
    private long mileageTotal;

    @NotNull
    private Double drivingStyleScore;

    @NotNull
    private Double lat;

    @NotNull
    private Double lng;
}
