package com.business.core.service.impl;

import com.business.core.domain.BillingItem;
import com.business.core.domain.Customer;
import com.business.core.domain.Dashboard;
import com.business.core.domain.Invoice;
import com.business.core.domain.Product;
import com.business.core.repository.DashboardRepository;
import com.business.core.repository.InvoiceRepository;
import com.business.core.repository.ProductRepository;
import com.business.core.service.InvoiceService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.business.core.domain.Invoice}.
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    private final InvoiceRepository invoiceRepository;

    private final ProductRepository productRepository;

    private final DashboardRepository dashboardRepository;

    public InvoiceServiceImpl(
        InvoiceRepository invoiceRepository,
        ProductRepository productRepository,
        DashboardRepository dashboardRepository
    ) {
        this.invoiceRepository = invoiceRepository;
        this.productRepository = productRepository;
        this.dashboardRepository = dashboardRepository;
    }

    @Override
    public Invoice save(Invoice invoice) {
        log.debug("Request to save Invoice : {}", invoice);

        List<Product> products = new ArrayList<Product>();
        // Dashboard dashboard = dashboardRepository.findByBusinessId(invoice.getBusiness().getId());
        invoice
            .getBillingItems()
            .forEach(item -> {
                Product product = item.getProduct();
                product.setOpeningQuantity(product.getOpeningQuantity() - item.getQuantity());
                products.add(item.getProduct());
                // dashboard.setTotalSaleTillDate(dashboard.getTotalSaleTillDate() + (product.getSellingPrice() * item.getQuantity()));

                // Double totalProfit = dashboard.getTotalProfitTillDate()+(product.getSellingPrice() * item.getQuantity() - product.getPurchasePrice() * item.getQuantity());

                // dashboard.setTotalProfitTillDate(totalProfit);
            });

        productRepository.saveAll(products);
        // dashboardRepository.save(dashboard);

        invoice.setInvoiceNumber("0");
        String priviousInvcNumber = invoiceRepository.findFirstByOrderByIssueDateDesc().orElse(invoice).getInvoiceNumber();
        String invoiceNmber = Long.parseLong(priviousInvcNumber) + 1 + "";
        invoice.setInvoiceNumber("0" + invoiceNmber);
        Invoice returInvoice = invoiceRepository.save(invoice);

        return returInvoice;
    }

    @Override
    public Invoice update(Invoice invoice) {
        log.debug("Request to update Invoice : {}", invoice);
        return invoiceRepository.save(invoice);
    }

    @Override
    public Optional<Invoice> partialUpdate(Invoice invoice) {
        log.debug("Request to partially update Invoice : {}", invoice);

        return invoiceRepository
            .findById(invoice.getId())
            .map(existingInvoice -> {
                if (invoice.getInvoiceNumber() != null) {
                    existingInvoice.setInvoiceNumber(invoice.getInvoiceNumber());
                }
                if (invoice.getIssueDate() != null) {
                    existingInvoice.setIssueDate(invoice.getIssueDate());
                }
                if (invoice.getDueDate() != null) {
                    existingInvoice.setDueDate(invoice.getDueDate());
                }
                if (invoice.getTotalAmount() != null) {
                    existingInvoice.setTotalAmount(invoice.getTotalAmount());
                }

                return existingInvoice;
            })
            .map(invoiceRepository::save);
    }

    @Override
    public Page<Invoice> findAll(Pageable pageable, String businessId) {
        log.debug("Request to get all Invoices");
        return invoiceRepository.findByBusinessId(pageable, businessId);
    }

    @Override
    public Optional<Invoice> findOne(String id) {
        log.debug("Request to get Invoice : {}", id);
        return invoiceRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Invoice : {}", id);
        invoiceRepository.deleteById(id);
    }

    @Override
    public Optional<List<Invoice>> findByCustomerOrderByIssueDateDesc(Customer customer, String businessId) {
        log.debug("Request to get all Invoices By Customer");
        return invoiceRepository.findByCustomerIdAndBusinessId(customer, businessId);
    }
}
