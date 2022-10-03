package nl.bss.carrentapi.api.repository;

import nl.bss.carrentapi.api.models.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    public List<Invoice> findByOwnerId(long userId);

    public List<Invoice> findByRenterId(long userId);
}
