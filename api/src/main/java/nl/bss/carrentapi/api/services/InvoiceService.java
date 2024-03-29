package nl.bss.carrentapi.api.services;

import lombok.AllArgsConstructor;
import nl.bss.carrentapi.api.exceptions.NotAllowedException;
import nl.bss.carrentapi.api.exceptions.NotFoundException;
import nl.bss.carrentapi.api.models.Invoice;
import nl.bss.carrentapi.api.models.Rental;
import nl.bss.carrentapi.api.models.User;
import nl.bss.carrentapi.api.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    /**
     * Creates a new invoice with given information. Still needs to be saved in order to persist.
     */
    public Invoice createInvoice(long mileageTotal, Double initialCost, Double mileageCosts, long kmPackage, Double totalHourPrice, Double totalHoursUsed, Double totalPrice, Boolean isPaid, User renter, User owner, Rental rental) {
        return new Invoice(mileageTotal, initialCost, mileageCosts, kmPackage, totalHourPrice, totalHoursUsed, totalPrice, isPaid, renter, owner, rental);
    }

    /**
     * Marks the invoice as paid if the user still has to pay this invoice and he is the one who should pay it.
     *
     * @param user
     * @param invoiceId
     */
    public Invoice payInvoice(User user, long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new NotFoundException("This invoice was not found."));

        ensureIsUserIsRenterForInvoice(invoice, user);

        ensureIsInvoiceNotPaidYet(invoice);

        invoice.setIsPaid(true);
        return invoiceRepository.save(invoice);
    }

    public void ensureIsUserIsRenterForInvoice(Invoice invoice, User user) {
        if (invoice.getRenter() != user) {
            throw new NotAllowedException("This is not your invoice to pay.");
        }
    }

    public void ensureIsInvoiceNotPaidYet(Invoice invoice) {
        if (invoice.getIsPaid()) {
            throw new NotAllowedException("You can't pay this invoice.");
        }
    }

    public List<Invoice> getInvoicesByOwner(User user) {
        return invoiceRepository.findByOwnerId(user.getId());
    }

    public List<Invoice> getInvoicesByRenter(User user) {
        return invoiceRepository.findByRenterId(user.getId());
    }
}
