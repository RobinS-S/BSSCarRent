package nl.bss.carrentapi.api.models.dto;

import java.math.BigDecimal;

public class InvoiceDto {
    private long id;

    private BigDecimal mileageTotal;

    private BigDecimal mileageCosts;

    private long kmPackage;

    private BigDecimal overKmPackageCosts;

    private BigDecimal totalPrice;

    private Boolean isPaid;

    private long renterId;

    private long ownerId;

    private long rentalId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getMileageTotal() {
        return mileageTotal;
    }

    public void setMileageTotal(BigDecimal mileageTotal) {
        this.mileageTotal = mileageTotal;
    }

    public BigDecimal getMileageCosts() {
        return mileageCosts;
    }

    public void setMileageCosts(BigDecimal mileageCosts) {
        this.mileageCosts = mileageCosts;
    }

    public long getKmPackage() {
        return kmPackage;
    }

    public void setKmPackage(long kmPackage) {
        this.kmPackage = kmPackage;
    }

    public BigDecimal getOverKmPackageCosts() {
        return overKmPackageCosts;
    }

    public void setOverKmPackageCosts(BigDecimal overKmPackageCosts) {
        this.overKmPackageCosts = overKmPackageCosts;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public long getRenterId() {
        return renterId;
    }

    public void setRenterId(long renterId) {
        this.renterId = renterId;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public long getRental() {
        return rentalId;
    }

    public void setRental(long rental) {
        this.rentalId = rentalId;
    }
}
