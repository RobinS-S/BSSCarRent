package nl.bss.carrentapi.api.services.interfaces;

import nl.bss.carrentapi.api.models.Invoice;
import nl.bss.carrentapi.api.models.Rental;
import nl.bss.carrentapi.api.models.User;

public interface InvoiceService {
    Invoice createInvoice(long mileageTotal, Double mileageCosts, long kmPackage, Double overKmPackageCosts, Double totalPrice, Boolean isPaid, User renter, User owner, Rental rental);
}
