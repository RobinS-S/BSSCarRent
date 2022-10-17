package nl.bss.carrentapi.api.interfaces;

public interface CostCalculable {
    Double calculateMonthlyCost();

    Double calculateCostForKms(long kms);
}
