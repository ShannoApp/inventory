package com.business.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Product.
 */
@Document(collection = "product")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(min = 2)
    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("unit")
    private String unit;

    @NotNull
    @DecimalMin(value = "0")
    @Field("sellingPrice")
    private Double sellingPrice;

    @NotNull
    @DecimalMin(value = "0")
    @Field("purchasePrice")
    private Double purchasePrice;

    @NotNull
    @DecimalMin(value = "0")
    @Field("minStockToMaintain")
    private Double minStockToMaintain;

    @NotNull
    @Min(value = 0)
    @Field("openingQuantity")
    private Integer openingQuantity;

    @NotNull
    @Field("asOfDate")
    private LocalDate asOfDate;

    @Min(value = 0)
    @Field("location")
    private String location;

    private BillingItem billingItem;

    @DBRef
    @Field("business")
    @JsonIgnoreProperties(value = { "customers", "products", "categories", "invoices" }, allowSetters = true)
    private Business business;

    @DBRef
    @Field("category")
    @JsonIgnoreProperties(value = { "products", "business" }, allowSetters = true)
    private Category category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Product id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return this.unit;
    }

    public Product unit(String unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getSellingPrice() {
        return this.sellingPrice;
    }

    public Product sellingPrice(Double sellingPrice) {
        this.setSellingPrice(sellingPrice);
        return this;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Double getPurchasePrice() {
        return this.purchasePrice;
    }

    public Product purchasePrice(Double purchasePrice) {
        this.setSellingPrice(purchasePrice);
        return this;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getOpeningQuantity() {
        return this.openingQuantity;
    }

    public Product openingQuantity(Integer openingQuantity) {
        this.setOpeningQuantity(openingQuantity);
        return this;
    }

    public void setOpeningQuantity(Integer openingQuantity) {
        this.openingQuantity = openingQuantity;
    }

    public Double getMinStockToMaintain() {
        return this.minStockToMaintain;
    }

    public Product minStockToMaintain(Double minStockToMaintain) {
        this.setMinStockToMaintain(minStockToMaintain);
        return this;
    }

    public void setMinStockToMaintain(Double minStockToMaintain) {
        this.minStockToMaintain = minStockToMaintain;
    }

    public LocalDate getAsOfDate() {
        return this.asOfDate;
    }

    public Product asOfDate(LocalDate asOfDate) {
        this.setAsOfDate(asOfDate);
        return this;
    }

    public void setAsOfDate(LocalDate asOfDate) {
        this.asOfDate = asOfDate;
    }

    public String getlocation() {
        return this.location;
    }

    public Product location(String location) {
        this.setlocation(location);
        return this;
    }

    public void setlocation(String location) {
        this.location = location;
    }

    public BillingItem getBillingItem() {
        return this.billingItem;
    }

    public void setBillingItem(BillingItem billingItem) {
        if (this.billingItem != null) {
            this.billingItem.setProduct(null);
        }
        if (billingItem != null) {
            billingItem.setProduct(this);
        }
        this.billingItem = billingItem;
    }

    public Product billingItem(BillingItem billingItem) {
        this.setBillingItem(billingItem);
        return this;
    }

    public Business getBusiness() {
        return this.business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Product business(Business business) {
        this.setBusiness(business);
        return this;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product category(Category category) {
        this.setCategory(category);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Product [id=" +
            id +
            ", name=" +
            name +
            ", description=" +
            description +
            ", unit=" +
            unit +
            ", sellingPrice=" +
            sellingPrice +
            ", purchasePrice=" +
            purchasePrice +
            ", minStockToMaintain=" +
            minStockToMaintain +
            ", openingQuantity=" +
            openingQuantity +
            ", asOfDate=" +
            asOfDate +
            ", location=" +
            location +
            ", billingItem=" +
            billingItem +
            ", business=" +
            business +
            ", category=" +
            category +
            "]"
        );
    }
    // prettier-ignore

}
