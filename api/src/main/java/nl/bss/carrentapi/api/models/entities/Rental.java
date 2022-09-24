package nl.bss.carrentapi.api.models.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rentals", schema = "PUBLIC")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column
    private LocalDateTime reservedFrom;

    @Column
    private LocalDateTime reservedUntil;

    @Column
    private LocalDateTime deliveredAt;

    @Column
    private long mileageProduced;

    @Column
    private long drivingStyleScore;

    @Column
    private long kmPackage;

    @Column
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private User tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    protected Rental() {}

    public Rental(LocalDateTime reservedFrom, LocalDateTime reservedUntil, LocalDateTime deliveredAt, long mileageProduced, long drivingStyleScore, long kmPackage, boolean isActive, User tenant) {
        this.reservedFrom = reservedFrom;
        this.reservedUntil = reservedUntil;
        this.deliveredAt = deliveredAt;
        this.mileageProduced = mileageProduced;
        this.drivingStyleScore = drivingStyleScore;
        this.kmPackage = kmPackage;
        this.isActive = isActive;
        this.tenant = tenant;
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

    public long getMileageProduced() {
        return mileageProduced;
    }

    public void setMileageProduced(long mileageProduced) {
        this.mileageProduced = mileageProduced;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
