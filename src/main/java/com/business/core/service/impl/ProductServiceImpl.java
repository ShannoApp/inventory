package com.business.core.service.impl;

import com.business.core.domain.Product;
import com.business.core.repository.ProductRepository;
import com.business.core.service.ProductService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.business.core.domain.Product}.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        log.debug("Request to save Product : {}", product);
        return productRepository.save(product);
    }

    @Override
    public Product update(Product product) {
        log.debug("Request to update Product : {}", product);
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> partialUpdate(Product product) {
        log.debug("Request to partially update Product : {}", product);

        return productRepository
            .findById(product.getId())
            .map(existingProduct -> {
                if (product.getName() != null) {
                    existingProduct.setName(product.getName());
                }
                if (product.getDescription() != null) {
                    existingProduct.setDescription(product.getDescription());
                }
                if (product.getSellingPrice() != null) {
                    existingProduct.setSellingPrice(product.getSellingPrice());
                }
                if (product.getOpeningQuantity() != null) {
                    existingProduct.setOpeningQuantity(product.getOpeningQuantity());
                }

                return existingProduct;
            })
            .map(productRepository::save);
    }

    @Override
    public Page<Product> findAll(Pageable pageable, String businessId) {
        log.debug("Request to get all Products");
        return productRepository.findByBusinessId(pageable, businessId);
    }

    /**
     *  Get all the products where BillingItem is {@code null}.
     *  @return the list of entities.
     */

    public List<Product> findAllWhereBillingItemIsNull() {
        log.debug("Request to get all products where BillingItem is null");
        return StreamSupport
            .stream(productRepository.findAll().spliterator(), false)
            .filter(product -> product.getBillingItem() == null)
            .toList();
    }

    @Override
    public Optional<Product> findOne(String id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }
}
