package nl.bss.carrentapi.api.calculations;

import nl.bss.carrentapi.api.models.FuelCellCar;
import nl.bss.carrentapi.api.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculateCostFCEV {

    FuelCellCar car;

    @BeforeEach
    void setUp() {
        car = new FuelCellCar("Opel", "Corsa", "Spacegray", "AB-123-C", 10000, 1.0, 15.0, 10.0, LocalDate.now(), LocalDate.now(), 1.0, 1.0,
                new User("test@test.nl", "password", "Julian", "", "Bos", "+31", "6 25354555", LocalDate.now()));
    }

    @Test
    @DisplayName("Simple multiplication")
    void calculateCost() {
        assertEquals(75.5, car.calculateCostForKms(5), "Expect cost to be 75.5");
    }

    @Test
    @DisplayName("Multiplication by zero")
    void calculateCostWithZero() {
        assertEquals(0, car.calculateCostForKms(0), "Expect cost to be 0");
    }
}
