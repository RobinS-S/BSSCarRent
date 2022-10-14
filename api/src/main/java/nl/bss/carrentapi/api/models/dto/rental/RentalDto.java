package nl.bss.carrentapi.api.models.dto.rental;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class RentalDto {
    private long id;
    @NotNull
    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reservedFrom;
    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reservedUntil;
    private LocalDateTime deliveredAt;
    private long mileageTotal;
    private Double drivingStyleScore;
    private long kmPackage;
    private long tenantId;
    private long carOwnerId;
    private long carId;
    private LocalDateTime pickedUpAt;
    private boolean isCancelled;

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

    public Double getDrivingStyleScore() {
        return drivingStyleScore;
    }

    public void setDrivingStyleScore(Double drivingStyleScore) {
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

    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
    }

    public long getCarOwnerId() {
        return carOwnerId;
    }

    public void setCarOwnerId(long carOwnerId) {
        this.carOwnerId = carOwnerId;
    }

    public long getCarId() {
        return carId;
    }

    public void setCarId(long carId) {
        this.carId = carId;
    }

    public LocalDateTime isPickedUpAt() { return pickedUpAt; }

    public void setPickedUpAt(LocalDateTime pickedUpAt) { this.pickedUpAt = pickedUpAt; }

    public boolean isCancelled() { return isCancelled; }

    public void setIsCancelled(boolean cancelled) { isCancelled = cancelled; }
}
