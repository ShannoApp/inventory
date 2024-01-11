package com.business.core.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Dashboard.
 */
@Document(collection = "dashboard")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Dashboard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("business_id")
    private String businessId;

    @Field("total_sale_till_date")
    private Double totalSaleTillDate;

    @Field("total_profit_till_date")
    private Double totalProfitTillDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Dashboard id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessId() {
        return this.businessId;
    }

    public Dashboard businessId(String businessId) {
        this.setId(businessId);
        return this;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Double getTotalSaleTillDate() {
        return this.totalSaleTillDate;
    }

    public Dashboard totalSaleTillDate(Double totalSaleTillDate) {
        this.setTotalSaleTillDate(totalSaleTillDate);
        return this;
    }

    public void setTotalSaleTillDate(Double totalSaleTillDate) {
        this.totalSaleTillDate = totalSaleTillDate;
    }

    public Double getTotalProfitTillDate() {
        return this.totalProfitTillDate;
    }

    public Dashboard totalProfitTillDate(Double totalProfitTillDate) {
        this.setTotalProfitTillDate(totalProfitTillDate);
        return this;
    }

    public void setTotalProfitTillDate(Double totalProfitTillDate) {
        this.totalProfitTillDate = totalProfitTillDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dashboard)) {
            return false;
        }
        return getId() != null && getId().equals(((Dashboard) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dashboard{" +
            "id=" + getId() +
            ", totalSaleTillDate=" + getTotalSaleTillDate() +
            ", totalProfitTillDate=" + getTotalProfitTillDate() +
            "}";
    }
}
