package nl.bss.carrentapi.api.models.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "invoices", schema = "PUBLIC")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column
    private BigDecimal mileageTotal;

    @Column
    private BigDecimal mileageCosts;

    @Column
    private long kmPackage;

    @Column
    private BigDecimal overKmPackageCosts;

    @Column
    private BigDecimal totalPrice;

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

    public Invoice(BigDecimal mileageTotal, BigDecimal mileageCosts, long kmPackage, BigDecimal overKmPackageCosts, BigDecimal totalPrice, Boolean isPaid, User renter, User owner, Rental rental) {
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

    public User getRenter() {
        return renter;
    }

    public void setRenter(User renter) {
        this.renter = renter;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }
}
