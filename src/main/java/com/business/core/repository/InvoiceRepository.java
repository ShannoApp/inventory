package com.business.core.repository;

import com.business.core.domain.Customer;
import com.business.core.domain.Invoice;
import com.business.core.domain.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Invoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceRepository extends MongoRepository<Invoice, String> {
    Page<Invoice> findByBusinessId(Pageable pageable, String businessId);

    Optional<Invoice> findFirstByOrderByIssueDateDesc();

    Optional<List<Invoice>> findByCustomerIdAndBusinessId(Customer customer, String businessId);
}
