package nl.bss.carrentapi.api.services;

import nl.bss.carrentapi.api.models.entities.Invoice;
import nl.bss.carrentapi.api.models.entities.Rental;
import nl.bss.carrentapi.api.models.entities.User;
import nl.bss.carrentapi.api.services.interfaces.InvoiceService;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    /**
     * Creates a new invoice with given information. Still needs to be saved in order to persist.
     */
    @Override
    public Invoice createInvoice(long mileageTotal, Double mileageCosts, long kmPackage, Double overKmPackageCosts, Double totalPrice, Boolean isPaid, User renter, User owner, Rental rental) {
        return new Invoice(mileageTotal, mileageCosts, kmPackage, overKmPackageCosts, totalPrice, isPaid, renter, owner, rental);
    }
}
