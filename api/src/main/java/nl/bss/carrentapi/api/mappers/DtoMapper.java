package nl.bss.carrentapi.api.mappers;

import nl.bss.carrentapi.api.enums.CarType;
import nl.bss.carrentapi.api.models.dto.CarDto;
import nl.bss.carrentapi.api.models.dto.InvoiceDto;
import nl.bss.carrentapi.api.models.dto.PersonDto;
import nl.bss.carrentapi.api.models.dto.RentalDto;
import nl.bss.carrentapi.api.models.entities.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {
    private final ModelMapper modelMapper;

    public DtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PersonDto convertToDto(User user) {
        PersonDto dto = modelMapper.map(user, PersonDto.class);
        dto.setPhoneNumber(String.format("+%s%s", user.getPhoneInternationalCode(), user.getPhoneNumber()));
        return dto;
    }

    public CarDto convertToDto(Car car) {
        CarDto dto = modelMapper.map(car, CarDto.class);
        CarType type;
        if (car instanceof CombustionCar) {
            type = CarType.COMUBUSTION;
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

    public InvoiceDto convertToDto(Invoice invoice) {
        InvoiceDto dto = modelMapper.map(invoice, InvoiceDto.class);
        return dto;
    }
}
