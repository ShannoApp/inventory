package com.business.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Business.
 */
@Document(collection = "business")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Business implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(min = 2)
    @Field("name")
    private String name;

    @Field("website")
    private String website;

    @Field("email")
    private String email;

    @Field("isDeflaut")
    private boolean isDeflaut;

    @Field("phone")
    private String phone;

    @Field("userIds")
    private Set<String> users = new HashSet<>();

    @DBRef
    @Field("customer")
    @JsonIgnoreProperties(value = { "invoice", "business" }, allowSetters = true)
    private Set<Customer> customers = new HashSet<>();

    @DBRef
    @Field("product")
    @JsonIgnoreProperties(value = { "billingItem", "business", "category" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @DBRef
    @Field("category")
    @JsonIgnoreProperties(value = { "products", "business" }, allowSetters = true)
    private Set<Category> categories = new HashSet<>();

    @DBRef
    @Field("invoice")
    @JsonIgnoreProperties(value = { "customer", "billingItems", "business" }, allowSetters = true)
    private Set<Invoice> invoices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Business id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Business name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return this.website;
    }

    public Business website(String website) {
        this.setWebsite(website);
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return this.email;
    }

    public Business email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public Business phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Customer> getCustomers() {
        return this.customers;
    }

    public void setCustomers(Set<Customer> customers) {
        if (this.customers != null) {
            this.customers.forEach(i -> i.setBusiness(null));
        }
        if (customers != null) {
            customers.forEach(i -> i.setBusiness(this));
        }
        this.customers = customers;
    }

    public Business customers(Set<Customer> customers) {
        this.setCustomers(customers);
        return this;
    }

    public Business addCustomer(Customer customer) {
        this.customers.add(customer);
        customer.setBusiness(this);
        return this;
    }

    public Business removeCustomer(Customer customer) {
        this.customers.remove(customer);
        customer.setBusiness(null);
        return this;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setBusiness(null));
        }
        if (products != null) {
            products.forEach(i -> i.setBusiness(this));
        }
        this.products = products;
    }

    public Business products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Business addProduct(Product product) {
        this.products.add(product);
        product.setBusiness(this);
        return this;
    }

    public Business removeProduct(Product product) {
        this.products.remove(product);
        product.setBusiness(null);
        return this;
    }

    public Set<Category> getCategories() {
        return this.categories;
    }

    public void setCategories(Set<Category> categories) {
        if (this.categories != null) {
            this.categories.forEach(i -> i.setBusiness(null));
        }
        if (categories != null) {
            categories.forEach(i -> i.setBusiness(this));
        }
        this.categories = categories;
    }

    public Business categories(Set<Category> categories) {
        this.setCategories(categories);
        return this;
    }

    public Business addCategory(Category category) {
        this.categories.add(category);
        category.setBusiness(this);
        return this;
    }

    public Business removeCategory(Category category) {
        this.categories.remove(category);
        category.setBusiness(null);
        return this;
    }

    public Set<Invoice> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        if (this.invoices != null) {
            this.invoices.forEach(i -> i.setBusiness(null));
        }
        if (invoices != null) {
            invoices.forEach(i -> i.setBusiness(this));
        }
        this.invoices = invoices;
    }

    public Business invoices(Set<Invoice> invoices) {
        this.setInvoices(invoices);
        return this;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }

    public Business users(Set<Invoice> users) {
        this.setInvoices(users);
        return this;
    }

    public boolean isDeflaut() {
        return isDeflaut;
    }

    public void setDeflaut(boolean isDeflaut) {
        this.isDeflaut = isDeflaut;
    }

    public Business addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setBusiness(this);
        return this;
    }

    public Business removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.setBusiness(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Business)) {
            return false;
        }
        return getId() != null && getId().equals(((Business) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Business [id=" +
            id +
            ", name=" +
            name +
            ", website=" +
            website +
            ", email=" +
            email +
            ", isDeflaut=" +
            isDeflaut +
            ", phone=" +
            phone +
            ", users=" +
            users +
            ", customers=" +
            customers +
            ", products=" +
            products +
            ", categories=" +
            categories +
            ", invoices=" +
            invoices +
            "]"
        );
    }
    // prettier-ignore
    //    @Override
    //    public String toString() {
    //        return "Business{" +
    //            "id=" + getId() +
    //            ", name='" + getName() + "'" +
    //            ", website='" + getWebsite() + "'" +
    //            ", email='" + getEmail() + "'" +
    //            ", phone='" + getPhone() + "'" +
    //            "}";
    //    }
}
