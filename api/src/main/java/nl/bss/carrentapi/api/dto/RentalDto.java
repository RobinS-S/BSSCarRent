package nl.bss.carrentapi.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RentalDto {
    private long id;
    private LocalDateTime reservedFrom;
    private LocalDateTime reservedUntil;
    private LocalDateTime deliveredAt;
    private long mileageTotal;
    private Double drivingStyleScore;
    private long kmPackage;
    private long tenantId;
    private long carId;
}
