package nl.bss.carrentapi.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceDto {
    private long id;
    private Double mileageTotal;
    private Double mileageCosts;
    private long kmPackage;
    private Double overKmPackageCosts;
    private Double totalPrice;
    private Boolean isPaid;
    private long renterId;
    private long ownerId;
    private long rentalId;
}
