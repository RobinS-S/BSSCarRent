package nl.bss.carrentapi.api.services.interfaces;

import nl.bss.carrentapi.api.models.entities.Invoice;
import nl.bss.carrentapi.api.models.entities.Rental;
import nl.bss.carrentapi.api.models.entities.User;

import java.math.BigDecimal;

public interface InvoiceService {
    public Invoice createInvoice(BigDecimal mileageTotal, BigDecimal mileageCosts, long kmPackage, BigDecimal overKmPackageCosts, BigDecimal totalPrice, Boolean isPaid, User renter, User owner, Rental rental);
}
