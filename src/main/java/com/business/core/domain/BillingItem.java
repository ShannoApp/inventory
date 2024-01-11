package com.business.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A BillingItem.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BillingItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 2)
    @Field("description")
    private String description;

    @NotNull
    @Min(value = 1)
    @Field("quantity")
    private Integer quantity;

    @NotNull
    @DecimalMin(value = "0")
    @Field("unit_price")
    private Double unitPrice;

    @NotNull
    @DecimalMin(value = "0")
    @Field("total_amount")
    private Double totalAmount;

    @DBRef
    @Field("product")
    private Product product;

    // @DBRef
    // @Field("invoice")
    // @JsonIgnoreProperties(value = { "customer", "billingItems", "business" }, allowSetters = true)
    // private Invoice invoice;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getDescription() {
        return this.description;
    }

    public BillingItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public BillingItem quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return this.unitPrice;
    }

    public BillingItem unitPrice(Double unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getTotalAmount() {
        return this.totalAmount;
    }

    public BillingItem totalAmount(Double totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BillingItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    // public Invoice getInvoice() {
    //     return this.invoice;
    // }

    // public void setInvoice(Invoice invoice) {
    //     this.invoice = invoice;
    // }

    // public BillingItem invoice(Invoice invoice) {
    //     this.setInvoice(invoice);
    //     return this;
    // }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillingItem)) {
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillingItem{" +
            ", description='" + getDescription() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", totalAmount=" + getTotalAmount() +
            "}";
    }
}
