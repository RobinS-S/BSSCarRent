package nl.bss.carrentapi.api.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "invoices", schema = "PUBLIC")
@Getter
@Setter
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column
    private Double initialCost;

    @Column
    private long mileageTotal;

    @Column
    private Double mileageCosts;

    @Column
    private long kmPackage;

    @Column
    private Double totalPrice;

    @Column
    private Double totalHourPrice;

    @Column
    private Double totalHoursUsed;

    @Column
    private Boolean isPaid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id")
    private User renter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToOne
    private Rental rental;

    public Invoice(long mileageTotal, Double initialCost, Double mileageCosts, long kmPackage, Double totalHourPrice, Double totalHoursUsed, Double totalPrice, Boolean isPaid, User renter, User owner, Rental rental) {
        this.mileageTotal = mileageTotal;
        this.initialCost = initialCost;
        this.mileageCosts = mileageCosts;
        this.kmPackage = kmPackage;
        this.totalHoursUsed = totalHoursUsed;
        this.totalPrice = totalPrice;
        this.totalHourPrice = totalHourPrice;
        this.isPaid = isPaid;
        this.renter = renter;
        this.owner = owner;
        this.rental = rental;
    }

    protected Invoice() {
    }
}
