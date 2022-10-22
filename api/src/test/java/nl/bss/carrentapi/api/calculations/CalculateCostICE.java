package nl.bss.carrentapi.api.calculations;

import nl.bss.carrentapi.api.enums.CombustionFuelType;
import nl.bss.carrentapi.api.models.CombustionCar;
import nl.bss.carrentapi.api.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculateCostICE {

    CombustionCar car;

    @BeforeEach
    void setUp() {
        car = new CombustionCar("Opel", "Corsa", "Spacegray", "AB-123-C", 10000, 1.0, 15.0, 10.0, LocalDate.now(), LocalDate.now(), 1.0, 1.0, null,
                new User("test@test.nl", "password", "Julian", "", "Bos", "+31", "6 25354555", LocalDate.now()));
    }

    @Test
    @DisplayName("Simple multiplication for DIESEL")
    void calculateCostForDiesel() {
        car.setFuelType(CombustionFuelType.DIESEL);
        assertEquals(75.4, car.calculateCostForKms(5), "Expect cost to be 75.4");
    }

    @Test
    @DisplayName("Simple multiplication for GAS")
    void calculateCostForGas() {
        car.setFuelType(CombustionFuelType.GAS);
        assertEquals(75.35, car.calculateCostForKms(5), "Expect cost to be 75.35");
    }

    @Test
    @DisplayName("Simple multiplication for GASOLINE")
    void calculateCostForGasoline() {
        car.setFuelType(CombustionFuelType.GASOLINE);
        assertEquals(75.7, car.calculateCostForKms(5), "Expect cost to be 75.7");
    }

    @Test
    @DisplayName("Multiplication with 0 for DIESEL")
    void calculateCostForDieselWithZero() {
        car.setFuelType(CombustionFuelType.DIESEL);
        assertEquals(0, car.calculateCostForKms(0), "Expect cost to be 0");
    }

    @Test
    @DisplayName("Multiplication with 0 for GAS")
    void calculateCostForGasWithZero() {
        car.setFuelType(CombustionFuelType.GAS);
        assertEquals(0, car.calculateCostForKms(0), "Expect cost to be 0");
    }

    @Test
    @DisplayName("Multiplication with 0 for GASOLINE")
    void calculateCostForGasolineWithZero() {
        car.setFuelType(CombustionFuelType.GASOLINE);
        assertEquals(0, car.calculateCostForKms(0), "Expect cost to be 0");
    }

}
