package nl.bss.carrentapi.api.mappers;

import nl.bss.carrentapi.api.dto.InvoiceDto;
import nl.bss.carrentapi.api.dto.rental.RentalDto;
import nl.bss.carrentapi.api.dto.car.CarDto;
import nl.bss.carrentapi.api.dto.rental.RentalPeriodDto;
import nl.bss.carrentapi.api.dto.user.UserDto;
import nl.bss.carrentapi.api.enums.CarType;
import nl.bss.carrentapi.api.models.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {
    private final ModelMapper modelMapper;

    public DtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
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
        return modelMapper.map(rental, RentalDto.class);
    }

    public RentalPeriodDto convertToRentalPeriodDto(Rental rental) {
        return modelMapper.map(rental, RentalPeriodDto.class);
    }

    public InvoiceDto convertToDto(Invoice invoice) {
        return modelMapper.map(invoice, InvoiceDto.class);
    }
}
