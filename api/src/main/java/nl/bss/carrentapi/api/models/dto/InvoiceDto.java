package nl.bss.carrentapi.api.models.dto;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getMileageTotal() {
        return mileageTotal;
    }

    public void setMileageTotal(Double mileageTotal) {
        this.mileageTotal = mileageTotal;
    }

    public Double getMileageCosts() {
        return mileageCosts;
    }

    public void setMileageCosts(Double mileageCosts) {
        this.mileageCosts = mileageCosts;
    }

    public long getKmPackage() {
        return kmPackage;
    }

    public void setKmPackage(long kmPackage) {
        this.kmPackage = kmPackage;
    }

    public Double getOverKmPackageCosts() {
        return overKmPackageCosts;
    }

    public void setOverKmPackageCosts(Double overKmPackageCosts) {
        this.overKmPackageCosts = overKmPackageCosts;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
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

    public long getRentalId() {
        return rentalId;
    }

    public void setRentalId(long rental) {
        this.rentalId = rentalId;
    }
}
