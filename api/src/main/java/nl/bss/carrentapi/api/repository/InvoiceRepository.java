package nl.bss.carrentapi.api.repository;

import nl.bss.carrentapi.api.models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByOwnerId(long userId);

    List<Invoice> findByRenterId(long userId);
}
