package nl.bss.carrentapi.api.services;

import lombok.AllArgsConstructor;
import nl.bss.carrentapi.api.exceptions.NotAllowedException;
import nl.bss.carrentapi.api.exceptions.NotFoundException;
import nl.bss.carrentapi.api.models.Invoice;
import nl.bss.carrentapi.api.models.Rental;
import nl.bss.carrentapi.api.models.User;
import nl.bss.carrentapi.api.repository.InvoiceRepository;
import nl.bss.carrentapi.api.services.interfaces.InvoiceService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;

    /**
     * Creates a new invoice with given information. Still needs to be saved in order to persist.
     */
    @Override
    public Invoice createInvoice(long mileageTotal, Double initialCost, Double mileageCosts, long kmPackage, Double totalHourPrice, Double totalHoursUsed, Double totalPrice, Boolean isPaid, User renter, User owner, Rental rental) {
        return new Invoice(mileageTotal, initialCost, mileageCosts, kmPackage, totalHourPrice, totalHoursUsed, totalPrice, isPaid, renter, owner, rental);
    }

    @Override
    public Invoice payInvoice(User user, long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new NotFoundException("This invoice was not found."));

        if (invoice.getRenter() != user) {
            throw new NotAllowedException("This is not your invoice to pay.");
        }

        if (invoice.getIsPaid()) {
            throw new NotAllowedException("You can't pay this invoice.");
        }

        invoice.setIsPaid(true);
        return invoiceRepository.save(invoice);
    }
}
