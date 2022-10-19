package nl.bss.carrentapi.api.services;

import nl.bss.carrentapi.api.models.Invoice;
import nl.bss.carrentapi.api.models.Rental;
import nl.bss.carrentapi.api.models.User;
import nl.bss.carrentapi.api.services.interfaces.InvoiceService;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    /**
     * Creates a new invoice with given information. Still needs to be saved in order to persist.
     */
    @Override
    public Invoice createInvoice(long mileageTotal, Double initialCost, Double mileageCosts, long kmPackage, Double totalHourPrice, Double totalHoursUsed, Double totalPrice, Boolean isPaid, User renter, User owner, Rental rental) {
        return new Invoice(mileageTotal, initialCost, mileageCosts, kmPackage, totalHourPrice, totalHoursUsed, totalPrice, isPaid, renter, owner, rental);
    }
}
