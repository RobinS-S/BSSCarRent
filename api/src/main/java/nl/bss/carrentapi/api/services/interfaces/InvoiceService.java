package nl.bss.carrentapi.api.services.interfaces;

import nl.bss.carrentapi.api.models.entities.Invoice;
import nl.bss.carrentapi.api.models.entities.Rental;
import nl.bss.carrentapi.api.models.entities.User;

public interface InvoiceService {
    public Invoice createInvoice(long mileageTotal, Double mileageCosts, long kmPackage, Double overKmPackageCosts, Double totalPrice, Boolean isPaid, User renter, User owner, Rental rental);
}
