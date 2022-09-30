package nl.bss.carrentapi.api.models.interfaces;

public interface CostCalculable {
    public Double calculateMonthlyCost();

    public Double calculateCostForKms(Double kms);
}
