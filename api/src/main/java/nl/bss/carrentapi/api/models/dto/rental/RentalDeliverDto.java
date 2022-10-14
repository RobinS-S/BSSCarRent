package nl.bss.carrentapi.api.models.dto.rental;

public class RentalDeliverDto {
    private long mileageTotal;
    private Double drivingStyleScore;

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
}
