package nl.bss.carrentapi.api.controllers;

import nl.bss.carrentapi.api.mappers.DtoMapper;
import nl.bss.carrentapi.api.models.dto.InvoiceDto;
import nl.bss.carrentapi.api.models.entities.Invoice;
import nl.bss.carrentapi.api.models.entities.User;
import nl.bss.carrentapi.api.repository.InvoiceRepository;
import nl.bss.carrentapi.api.services.interfaces.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/invoice")
@Validated
public class InvoiceController {
    private final DtoMapper dtoMapper;

    private final AuthService authService;
    private final InvoiceRepository invoiceRepository;

    public InvoiceController(DtoMapper dtoMapper,  AuthService authService, InvoiceRepository invoiceRepository) {
        this.dtoMapper = dtoMapper;
        this.authService = authService;
        this.invoiceRepository = invoiceRepository;
    }

    @GetMapping("/mine")
    public ResponseEntity<List<InvoiceDto>> findByOwnerId(@RequestHeader("Authorization") String authHeader) {
        Optional<User> foundUser = authService.getCurrentUserByAuthHeader(authHeader);
        if (foundUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        User user = foundUser.get();

        Set<Invoice> invoices = user.getRentalInvoices();
        return ResponseEntity.ok(invoices.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/owned")
    public ResponseEntity<List<InvoiceDto>> findByRenterId(@RequestHeader("Authorization") String authHeader) {
        Optional<User> foundUser = authService.getCurrentUserByAuthHeader(authHeader);
        if (foundUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        User user = foundUser.get();

        Set<Invoice> invoices = user.getRentOutInvoices();
        return ResponseEntity.ok(invoices.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<InvoiceDto> create(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
        Optional<User> foundUser = authService.getCurrentUserByAuthHeader(authHeader);
        if (foundUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<Invoice> foundInvoice = invoiceRepository.findById(id);
        if(foundInvoice.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Invoice invoice = foundInvoice.get();

        if(invoice.getPaid()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        invoice.setPaid(true);
        invoice = invoiceRepository.save(invoice);

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(invoice));
    }

}
