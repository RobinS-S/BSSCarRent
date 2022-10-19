package nl.bss.carrentapi.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceDto {
    private long id;
    private Double initialCost;
    private Double mileageTotal;
    private Double mileageCosts;
    private long kmPackage;
    private Double totalPrice;
    private Double totalHoursUsed;
    private Double totalHourPrice;
    private Boolean isPaid;
    private long renterId;
    private long ownerId;
    private long rentalId;
}
