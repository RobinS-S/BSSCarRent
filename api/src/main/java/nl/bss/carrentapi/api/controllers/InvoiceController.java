package nl.bss.carrentapi.api.controllers;

import lombok.AllArgsConstructor;
import nl.bss.carrentapi.api.dto.InvoiceDto;
import nl.bss.carrentapi.api.mappers.DtoMapper;
import nl.bss.carrentapi.api.models.Invoice;
import nl.bss.carrentapi.api.models.User;
import nl.bss.carrentapi.api.repository.InvoiceRepository;
import nl.bss.carrentapi.api.services.interfaces.AuthService;
import nl.bss.carrentapi.api.services.interfaces.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/invoice")
@Validated
@AllArgsConstructor
public class InvoiceController {
    private final DtoMapper dtoMapper;
    private final AuthService authService;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceService invoiceService;

    /**
     * Finds invoices where User owned the car
     */
    @GetMapping("/mine")
    public ResponseEntity<List<InvoiceDto>> findByOwnerId(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);

        List<Invoice> invoices = invoiceRepository.findByOwnerId(user.getId());
        return ResponseEntity.ok(invoices.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    /**
     * Finds invoices where User rented the car
     */
    @GetMapping("/owned")
    public ResponseEntity<List<InvoiceDto>> findByRenterId(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);

        List<Invoice> invoices = invoiceRepository.findByRenterId(user.getId());
        return ResponseEntity.ok(invoices.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    /**
     * Pays an Invoice
     */
    @PostMapping("/{id}/pay")
    public ResponseEntity<InvoiceDto> create(@RequestHeader(name = "Authorization", required = false) String authHeader, @PathVariable Long id) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);
        Invoice invoice = invoiceService.payInvoice(user, id);

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(invoice));
    }

}
