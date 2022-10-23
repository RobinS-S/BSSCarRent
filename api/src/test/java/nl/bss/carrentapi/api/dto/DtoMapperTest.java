package nl.bss.carrentapi.api.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.bss.carrentapi.api.dto.car.CarDto;
import nl.bss.carrentapi.api.dto.user.UserDto;
import nl.bss.carrentapi.api.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;

class DtoMapperTest {

    User user1;
    User user2;
    BatteryElectricCar batteryElectricCar;
    FuelCellCar fuelCellCar;
    Rental rental;
    Invoice invoice;

    @BeforeEach
    void setUp() {
        user1 = new User("test@test.nl", "password", "Julian", "", "Bos", "+31", "6 25354555", LocalDate.now());
        user2 = new User("owner@test.nl", "password", "Julian", "", "Bos", "+31", "6 25354555", LocalDate.now());
        batteryElectricCar = new BatteryElectricCar("Opel", "Corsa", "Spacegray", "AB-123-C", 10000, 1.0, 15.0, 10.0, LocalDate.now(), LocalDate.now(), 1.0, 1.0,user1);
        fuelCellCar = new FuelCellCar("Opel", "Corsa", "Red", "AB-123-C", 10000, 1.0, 15.0, 10.0, LocalDate.now(), LocalDate.now(), 1.0, 1.0, user1);
        rental = new Rental(LocalDateTime.now(), LocalDateTime.now(), 10000, user1, user2, batteryElectricCar);
        invoice = new Invoice(10, 10.0, 15.0, 100, 10.0, 3.5, 65.50, false, user1, user2, rental);
    }

    @Test
    @DisplayName("License check for batteryElectricCar")
    void DtoMapbatteryElectricCar() {
        ModelMapper modelMapper = new ModelMapper();
        CarDto dto = modelMapper.map(batteryElectricCar, CarDto.class);
        assertEquals("AB-123-C", dto.getLicensePlate() , "Expect license plate to be AB-123-C");
    }

    @Test
    @DisplayName("Check color for fuelCellCar")
    void dtoMapFuelCellCar() {
        ModelMapper modelMapper = new ModelMapper();
        CarDto dto = modelMapper.map(fuelCellCar, CarDto.class);
        assertEquals("Red", dto.getColor() , "Expect color to be red");
    }

    @Test
    @DisplayName("Check firstname of User")
    void dtoMapUser() {
        ModelMapper modelMapper = new ModelMapper();
        UserDto dto = modelMapper.map(user1, UserDto.class);
        assertEquals("Julian", dto.getFirstName() , "Expect name to be Julian");
    }

    @Test
    @DisplayName("Check if invoice is paid")
    void dtoMapInvoice() {
        ModelMapper modelMapper = new ModelMapper();
        InvoiceDto dto = modelMapper.map(invoice, InvoiceDto.class);
        assertEquals(false, dto.getIsPaid() , "Expect to be false (not paid)");
    }
}
