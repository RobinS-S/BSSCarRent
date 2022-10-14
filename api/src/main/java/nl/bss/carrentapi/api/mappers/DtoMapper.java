package nl.bss.carrentapi.api.mappers;

import nl.bss.carrentapi.api.enums.CarType;
import nl.bss.carrentapi.api.models.dto.InvoiceDto;
import nl.bss.carrentapi.api.models.dto.RentalDto;
import nl.bss.carrentapi.api.models.dto.car.CarDto;
import nl.bss.carrentapi.api.models.dto.rental.RentalPeriodDto;
import nl.bss.carrentapi.api.models.dto.user.UserDto;
import nl.bss.carrentapi.api.models.entities.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {
    private final ModelMapper modelMapper;

    public DtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDto convertToDto(User user) {
        UserDto dto = modelMapper.map(user, UserDto.class);
        return dto;
    }

    public CarDto convertToDto(Car car) {
        CarDto dto = modelMapper.map(car, CarDto.class);
        CarType type;
        if (car instanceof CombustionCar) {
            type = CarType.COMBUSTION;
            dto.setCarType(type);
        } else if (car instanceof BatteryElectricCar) {
            type = CarType.BATTERY_ELECTRIC;
            dto.setCarType(type);
        } else if (car instanceof FuelCellCar) {
            type = CarType.FUEL_CELL;
            dto.setCarType(type);
        }
        return dto;
    }

    public RentalDto convertToDto(Rental rental) {
        RentalDto dto = modelMapper.map(rental, RentalDto.class);
        return dto;
    }

    public RentalPeriodDto convertToRentalPeriodDto(Rental rental) {
        RentalPeriodDto dto = modelMapper.map(rental, RentalPeriodDto.class);
        return dto;
    }

    public InvoiceDto convertToDto(Invoice invoice) {
        InvoiceDto dto = modelMapper.map(invoice, InvoiceDto.class);
        return dto;
    }
}
