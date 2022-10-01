package nl.bss.carrentapi.api;

import nl.bss.carrentapi.api.models.dto.RentalDto;
import nl.bss.carrentapi.api.models.entities.Rental;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(Rental.class, RentalDto.class).addMappings(mapper -> {
        });
        return modelMapper;
    }

}