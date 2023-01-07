package nl.bss.carrentapi.api.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "images", schema = "PUBLIC")
@Getter
@Setter
public class CarImage {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    public Car car;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Lob
    @Column
    /**
     * Contains the raw image data. Caution: it can be big! Don't query this for all images!
     */
    private byte[] data;
    
    @Column
    private String type;

    public CarImage(String type) {
        this.type = type;
    }

    protected CarImage() {
    }
}
