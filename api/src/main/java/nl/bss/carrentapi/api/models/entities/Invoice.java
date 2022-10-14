package nl.bss.carrentapi.api.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "invoices", schema = "PUBLIC")
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMileageTotal() {
        return mileageTotal;
    }

    public void setMileageTotal(long mileageTotal) {
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
