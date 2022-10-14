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
    private LocalDateTime pickedUpAt;

    @Column
    private long mileageTotal;

    @Column
    private Double drivingStyleScore;

    @Column
    private long kmPackage;

    @Column
    private boolean isCancelled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private User tenant;

    /**
     * We store the Car's Owner here because at a later point we might still want to know who it belonged to at this time
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User carOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    protected Rental() {
    }

    public Rental(LocalDateTime reservedFrom, LocalDateTime reservedUntil, long kmPackage, User tenant, User carOwner, Car car) {
        this.reservedFrom = reservedFrom;
        this.reservedUntil = reservedUntil;
        this.kmPackage = kmPackage;
        this.tenant = tenant;
        this.carOwner = carOwner;
        this.car = car;
        this.isCancelled = false;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getTenant() {
        return tenant;
    }

    public void setTenant(User tenant) {
        this.tenant = tenant;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public User getCarOwner() { return carOwner; }

    public void setCarOwner(User carOwner) { this.carOwner = carOwner; }

    public LocalDateTime getPickedUpAt() { return pickedUpAt; }

    public void setPickedUpAt(LocalDateTime pickedUp) { this.pickedUpAt = pickedUp; }

    public boolean isCancelled() { return isCancelled; }

    public void setCancelled(boolean cancelled) { isCancelled = cancelled; }
}
