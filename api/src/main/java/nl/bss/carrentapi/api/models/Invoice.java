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
    private long mileageTotal;

    @Column
    private Double mileageCosts;

    @Column
    private long kmPackage;

    @Column
    private Double overKmPackageCosts;

    @Column
    private Double totalPrice;

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

    public Invoice(long mileageTotal, Double mileageCosts, long kmPackage, Double overKmPackageCosts, Double totalPrice, Boolean isPaid, User renter, User owner, Rental rental) {
        this.mileageTotal = mileageTotal;
        this.mileageCosts = mileageCosts;
        this.kmPackage = kmPackage;
        this.overKmPackageCosts = overKmPackageCosts;
        this.totalPrice = totalPrice;
        this.isPaid = isPaid;
        this.renter = renter;
        this.owner = owner;
        this.rental = rental;
    }

    protected Invoice() {
    }
}
