package nl.bss.carrentapi.api.controllers;

import lombok.AllArgsConstructor;
import nl.bss.carrentapi.api.dto.InvoiceDto;
import nl.bss.carrentapi.api.exceptions.NotAllowedException;
import nl.bss.carrentapi.api.mappers.DtoMapper;
import nl.bss.carrentapi.api.models.Invoice;
import nl.bss.carrentapi.api.models.User;
import nl.bss.carrentapi.api.repository.InvoiceRepository;
import nl.bss.carrentapi.api.services.interfaces.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/invoice")
@Validated
@AllArgsConstructor
public class InvoiceController {
    private final DtoMapper dtoMapper;
    private final AuthService authService;
    private final InvoiceRepository invoiceRepository;

    @GetMapping("/mine")
    public ResponseEntity<List<InvoiceDto>> findByOwnerId(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);

        List<Invoice> invoices = invoiceRepository.findByOwnerId(user.getId());
        return ResponseEntity.ok(invoices.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/owned")
    public ResponseEntity<List<InvoiceDto>> findByRenterId(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);

        List<Invoice> invoices = invoiceRepository.findByRenterId(user.getId());
        return ResponseEntity.ok(invoices.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<InvoiceDto> create(@RequestHeader(name = "Authorization", required = false) String authHeader, @PathVariable Long id) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);

        Optional<Invoice> foundInvoice = invoiceRepository.findById(id);
        if (foundInvoice.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Invoice invoice = foundInvoice.get();

        if (invoice.getRenter() != user) {
            throw new NotAllowedException("This is not your invoice to pay.");
        }

        if (invoice.getIsPaid()) {
            throw new NotAllowedException("You can't pay this invoice.");
        }

        invoice.setIsPaid(true);
        invoice = invoiceRepository.save(invoice);

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(invoice));
    }

}
