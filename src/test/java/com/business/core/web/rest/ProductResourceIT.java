package com.business.core.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.business.core.IntegrationTest;
import com.business.core.domain.Product;
import com.business.core.repository.ProductRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link ProductResource} REST controller.
 */
//@IntegrationTest
@SpringBootTest
@SpringJUnitConfig
@AutoConfigureMockMvc
@WithMockUser
class ProductResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_sellingPrice = 0D;
    private static final Double UPDATED_sellingPrice = 1D;

    private static final Integer DEFAULT_openingQuantity = 0;
    private static final Integer UPDATED_openingQuantity = 1;

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity() {
        Product product = new Product()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .sellingPrice(DEFAULT_sellingPrice)
            .openingQuantity(DEFAULT_openingQuantity)
            .asOfDate(LocalDate.now())
            .purchasePrice(DEFAULT_sellingPrice)
            .minStockToMaintain(1.0);
        return product;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity() {
        Product product = new Product()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .sellingPrice(UPDATED_sellingPrice)
            .openingQuantity(UPDATED_openingQuantity)
            .asOfDate(LocalDate.now())
            .purchasePrice(UPDATED_sellingPrice)
            .minStockToMaintain(2.0);
        return product;
    }

    @BeforeEach
    public void initTest() {
        productRepository.deleteAll();
        product = createEntity();
    }

    // @Test
    // void createProduct() throws Exception {
    //     int databaseSizeBeforeCreate = productRepository.findAll().size();
    //     // Create the Product
    //     restProductMockMvc
    //         .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
    //         .andExpect(status().isCreated());

    //     // Validate the Product in the database
    //     List<Product> productList = productRepository.findAll();
    //     assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
    //     Product testProduct = productList.get(productList.size() - 1);
    //     assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
    //     assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    //     assertThat(testProduct.getSellingPrice()).isEqualTo(DEFAULT_sellingPrice);
    //     assertThat(testProduct.getOpeningQuantity()).isEqualTo(DEFAULT_openingQuantity);
    // }

    @Test
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId("existing_id");

        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setName(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checksellingPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setSellingPrice(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkopeningQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setOpeningQuantity(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    //    @Test
    //    void getAllProducts() throws Exception {
    //        // Initialize the database
    //        productRepository.save(product);
    //
    //        // Get all the productList
    //        restProductMockMvc
    //            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
    //            .andExpect(status().isOk())
    //            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
    //            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId())))
    //            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
    //            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
    //            .andExpect(jsonPath("$.[*].sellingPrice").value(hasItem(DEFAULT_sellingPrice.doubleValue())))
    //            .andExpect(jsonPath("$.[*].openingQuantity").value(hasItem(DEFAULT_openingQuantity)));
    //    }

    // @Test
    // void getProduct() throws Exception {
    //     // Initialize the database
    //     productRepository.save(product);

    //     // Get the product
    //     restProductMockMvc
    //         .perform(get(ENTITY_API_URL_ID, product.getId()))
    //         .andExpect(status().isOk())
    //         .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
    //         .andExpect(jsonPath("$.id").value(product.getId()))
    //         .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
    //         .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
    //         .andExpect(jsonPath("$.sellingPrice").value(DEFAULT_sellingPrice.doubleValue()))
    //         .andExpect(jsonPath("$.openingQuantity").value(DEFAULT_openingQuantity));
    // }

    @Test
    void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    // @Test
    // void putExistingProduct() throws Exception {
    //     // Initialize the database
    //     productRepository.save(product);

    //     int databaseSizeBeforeUpdate = productRepository.findAll().size();

    //     // Update the product
    //     Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
    //     updatedProduct.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).sellingPrice(UPDATED_sellingPrice).openingQuantity(UPDATED_openingQuantity);

    //     restProductMockMvc
    //         .perform(
    //             put(ENTITY_API_URL_ID, updatedProduct.getId())
    //                 .contentType(MediaType.APPLICATION_JSON)
    //                 .content(TestUtil.convertObjectToJsonBytes(updatedProduct))
    //         )
    //         .andExpect(status().isOk());

    //     // Validate the Product in the database
    //     List<Product> productList = productRepository.findAll();
    //     assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    //     Product testProduct = productList.get(productList.size() - 1);
    //     assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
    //     assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    //     assertThat(testProduct.getSellingPrice()).isEqualTo(UPDATED_sellingPrice);
    //     assertThat(testProduct.getOpeningQuantity()).isEqualTo(UPDATED_openingQuantity);
    // }

    @Test
    void putNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, product.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    // @Test
    // void partialUpdateProductWithPatch() throws Exception {
    //     // Initialize the database
    //     productRepository.save(product);

    //     int databaseSizeBeforeUpdate = productRepository.findAll().size();

    //     // Update the product using partial update
    //     Product partialUpdatedProduct = new Product();
    //     partialUpdatedProduct.setId(product.getId());

    //     partialUpdatedProduct.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).openingQuantity(UPDATED_openingQuantity)
    //     .asOfDate(LocalDate.now())
    //     .purchasePrice(DEFAULT_sellingPrice).minStockToMaintain(1.0);;

    //     restProductMockMvc
    //         .perform(
    //             patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
    //                 .contentType("application/merge-patch+json")
    //                 .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
    //         )
    //         .andExpect(status().isOk());

    //     // Validate the Product in the database
    //     List<Product> productList = productRepository.findAll();
    //     assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    //     Product testProduct = productList.get(productList.size() - 1);
    //     assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
    //     assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    //     assertThat(testProduct.getSellingPrice()).isEqualTo(DEFAULT_sellingPrice);
    //     assertThat(testProduct.getOpeningQuantity()).isEqualTo(UPDATED_openingQuantity);
    // }

    // @Test
    // void fullUpdateProductWithPatch() throws Exception {
    //     // Initialize the database
    //     productRepository.save(product);

    //     int databaseSizeBeforeUpdate = productRepository.findAll().size();

    //     // Update the product using partial update
    //     Product partialUpdatedProduct = new Product();
    //     partialUpdatedProduct.setId(product.getId());

    //     partialUpdatedProduct.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).sellingPrice(UPDATED_sellingPrice)
    //     .openingQuantity(UPDATED_openingQuantity).asOfDate(LocalDate.now())
    //     .purchasePrice(DEFAULT_sellingPrice).minStockToMaintain(1.0);

    //     restProductMockMvc
    //         .perform(
    //             patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
    //                 .contentType("application/merge-patch+json")
    //                 .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
    //         )
    //         .andExpect(status().isOk());

    //     // Validate the Product in the database
    //     List<Product> productList = productRepository.findAll();
    //     assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    //     Product testProduct = productList.get(productList.size() - 1);
    //     assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
    //     assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    //     assertThat(testProduct.getSellingPrice()).isEqualTo(UPDATED_sellingPrice);
    //     assertThat(testProduct.getOpeningQuantity()).isEqualTo(UPDATED_openingQuantity);
    // }

    @Test
    void patchNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, product.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }
    // @Test
    // void deleteProduct() throws Exception {
    //     // Initialize the database
    //     productRepository.save(product);

    //     int databaseSizeBeforeDelete = productRepository.findAll().size();

    //     // Delete the product
    //     restProductMockMvc
    //         .perform(delete(ENTITY_API_URL_ID, product.getId()).accept(MediaType.APPLICATION_JSON))
    //         .andExpect(status().isNoContent());

    //     // Validate the database contains one less item
    //     List<Product> productList = productRepository.findAll();
    //     assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    // }
}
