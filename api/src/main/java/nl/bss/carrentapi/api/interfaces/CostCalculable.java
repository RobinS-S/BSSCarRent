package nl.bss.carrentapi.api.interfaces;

public interface CostCalculable {
    Double calculateAnnualCost();

    Double calculateCostForKms(long kms);
}
