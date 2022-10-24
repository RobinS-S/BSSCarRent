package nl.bss.carrentapi.api.repository;

import nl.bss.carrentapi.api.models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    /**
     * Finds all Invoices where the car is owned by userId
     *
     * @param userId
     * @return List of invoices
     */
    List<Invoice> findByOwnerId(long userId);

    /**
     * Finds all Invoices where the car is rented by userId
     *
     * @param userId
     * @return List of invoices
     */
    List<Invoice> findByRenterId(long userId);
}
