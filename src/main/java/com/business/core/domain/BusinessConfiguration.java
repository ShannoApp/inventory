package com.business.core.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A BusinessConfiguration.
 */
@Document(collection = "business_configuration")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BusinessConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("business_id")
    private String businessId;

    @Field("discount")
    private Double discount;

    @Field("shipping_charges")
    private Double shippingCharges;

    @Field("tax")
    private Double tax;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public BusinessConfiguration id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessId() {
        return this.businessId;
    }

    public BusinessConfiguration businessId(String businessId) {
        this.setBusinessId(businessId);
        return this;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Double getDiscount() {
        return this.discount;
    }

    public BusinessConfiguration discount(Double discount) {
        this.setDiscount(discount);
        return this;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getShippingCharges() {
        return this.shippingCharges;
    }

    public BusinessConfiguration shippingCharges(Double shippingCharges) {
        this.setShippingCharges(shippingCharges);
        return this;
    }

    public void setShippingCharges(Double shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    public Double getTax() {
        return this.tax;
    }

    public BusinessConfiguration tax(Double tax) {
        this.setTax(tax);
        return this;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessConfiguration)) {
            return false;
        }
        return getId() != null && getId().equals(((BusinessConfiguration) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessConfiguration{" +
            "id=" + getId() +
            ", businessId='" + getBusinessId() + "'" +
            ", discount=" + getDiscount() +
            ", shippingCharges=" + getShippingCharges() +
            ", tax=" + getTax() +
            "}";
    }
}
