package nl.bss.carrentapi.api.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rentals", schema = "PUBLIC")
@Getter
@Setter
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
}
