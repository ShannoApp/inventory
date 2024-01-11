package com.business.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Invoice.
 */
@Document(collection = "invoice")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    public static long COUNTER = 1L;

    @Id
    private String id;

    @Size(min = 2)
    @Field("invoice_number")
    private String invoiceNumber;

    @NotNull
    @Field("issue_date")
    private LocalDate issueDate;

    @NotNull
    @Field("due_date")
    private LocalDate dueDate;

    @NotNull
    @DecimalMin(value = "0")
    @Field("total_amount")
    private Double totalAmount;

    @NotNull
    @DecimalMin(value = "0")
    @Field("sub_total")
    private Double subTotal;

    @NotNull
    @DecimalMin(value = "0")
    @Field("discount")
    private Double discount;

    @NotNull
    @DecimalMin(value = "0")
    @Field("tax")
    private Double tax;

    @NotNull
    @Field("paid")
    private Boolean paid;

    @NotNull
    @DecimalMin(value = "0")
    @Field("shipping_charges")
    private Double shippingCharges;

    @DBRef
    @Field("customer")
    private Customer customer;

    @Field("billingItem")
    // @JsonIgnoreProperties(value = { "product", "invoice" }, allowSetters = true)
    private Set<BillingItem> billingItems = new HashSet<>();

    @DBRef
    @Field("business")
    @JsonIgnoreProperties(value = { "customers", "products", "categories", "invoices" }, allowSetters = true)
    private Business business;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Invoice id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public Invoice invoiceNumber(String invoiceNumber) {
        this.setInvoiceNumber(invoiceNumber);
        return this;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getIssueDate() {
        return this.issueDate;
    }

    public Invoice issueDate(LocalDate issueDate) {
        this.setIssueDate(issueDate);
        return this;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public Invoice dueDate(LocalDate dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Double getTotalAmount() {
        return this.totalAmount;
    }

    public Invoice totalAmount(Double totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public Invoice subTotal(Double subTotal) {
        this.setSubTotal(totalAmount);
        return this;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getDiscount() {
        return discount;
    }

    public Invoice discount(Double discount) {
        this.setDiscount(discount);
        return this;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTax() {
        return tax;
    }

    public Invoice tax(Double tax) {
        this.setTax(tax);
        return this;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getShippingCharges() {
        return shippingCharges;
    }

    public Invoice shippingCharges(Double shippingCharges) {
        this.setShippingCharges(shippingCharges);
        return this;
    }

    public void setShippingCharges(Double shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Invoice customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public Set<BillingItem> getBillingItems() {
        return this.billingItems;
    }

    public void setBillingItems(Set<BillingItem> billingItems) {
        // if (this.billingItems != null) {
        //     this.billingItems.forEach(i -> i.setInvoice(null));
        // }
        // if (billingItems != null) {
        //     billingItems.forEach(i -> i.setInvoice(this));
        // }
        this.billingItems = billingItems;
    }

    public Invoice billingItems(Set<BillingItem> billingItems) {
        this.setBillingItems(billingItems);
        return this;
    }

    public Invoice addBillingItem(BillingItem billingItem) {
        this.billingItems.add(billingItem);
        return this;
    }

    public Invoice removeBillingItem(BillingItem billingItem) {
        this.billingItems.remove(billingItem);
        return this;
    }

    public Business getBusiness() {
        return this.business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Invoice business(Business business) {
        this.setBusiness(business);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return getId() != null && getId().equals(((Invoice) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Invoice paid(Boolean paid) {
        this.setPaid(paid);
        return this;
    }

    @Override
    public String toString() {
        return (
            "Invoice [id=" +
            id +
            ", invoiceNumber=" +
            invoiceNumber +
            ", issueDate=" +
            issueDate +
            ", dueDate=" +
            dueDate +
            ", totalAmount=" +
            totalAmount +
            ", subTotal=" +
            subTotal +
            ", discount=" +
            discount +
            ", tax=" +
            tax +
            ", paid=" +
            paid +
            ", shippingCharges=" +
            shippingCharges +
            ", customer=" +
            customer +
            ", billingItems=" +
            billingItems +
            ", business=" +
            business +
            "]"
        );
    }
    // prettier-ignore

}
