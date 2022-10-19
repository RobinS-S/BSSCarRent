package nl.bss.carrentapi.api.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "images", schema = "PUBLIC")
@Getter
@Setter
public class CarImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Lob
    @Column
    private byte[] data;

    @Column
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    public Car car;

    public CarImage(String type) {
        this.type = type;
    }

    protected CarImage() {}
}
