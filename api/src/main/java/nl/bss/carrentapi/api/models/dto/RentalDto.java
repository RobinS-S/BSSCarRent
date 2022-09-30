package nl.bss.carrentapi.api.models.dto;

import java.time.LocalDateTime;

public class RentalDto {

    private long id;

    private LocalDateTime reservedFrom;

    private LocalDateTime reservedUntil;

    private LocalDateTime deliveredAt;

    private long mileageTotal;

    private long drivingStyleScore;

    private long kmPackage;

    private long tenantId;

    private long carId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getReservedFrom() {
        return reservedFrom;
    }

    public void setReservedFrom(LocalDateTime reservedFrom) {
        this.reservedFrom = reservedFrom;
    }

    public LocalDateTime getReservedUntil() {
        return reservedUntil;
    }

    public void setReservedUntil(LocalDateTime reservedUntil) {
        this.reservedUntil = reservedUntil;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public long getMileageTotal() {
        return mileageTotal;
    }

    public void setMileageTotal(long mileageTotal) {
        this.mileageTotal = mileageTotal;
    }

    public long getDrivingStyleScore() {
        return drivingStyleScore;
    }

    public void setDrivingStyleScore(long drivingStyleScore) {
        this.drivingStyleScore = drivingStyleScore;
    }

    public long getKmPackage() {
        return kmPackage;
    }

    public void setKmPackage(long kmPackage) {
        this.kmPackage = kmPackage;
    }

    public long getTenantId() {
        return tenantId;
    }

    public void SetTenantId(long tenantId) {
        this.tenantId = tenantId;
    }

    public long getCarId() {
        return carId;
    }

    public void setCarId(long carId) {
        this.carId = carId;
    }
}
