package com.business.core.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.business.core.IntegrationTest;
import com.business.core.domain.BusinessConfiguration;
import com.business.core.repository.BusinessConfigurationRepository;
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
 * Integration tests for the {@link BusinessConfigurationResource} REST controller.
 */
//@IntegrationTest
@SpringBootTest
@SpringJUnitConfig
@AutoConfigureMockMvc
@WithMockUser
class BusinessConfigurationResourceIT {

    private static final String DEFAULT_BUSINESS_ID = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_ID = "BBBBBBBBBB";

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;

    private static final Double DEFAULT_SHIPPING_CHARGES = 1D;
    private static final Double UPDATED_SHIPPING_CHARGES = 2D;

    private static final Double DEFAULT_TAX = 1D;
    private static final Double UPDATED_TAX = 2D;

    private static final String ENTITY_API_URL = "/api/business-configurations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private BusinessConfigurationRepository businessConfigurationRepository;

    @Autowired
    private MockMvc restBusinessConfigurationMockMvc;

    private BusinessConfiguration businessConfiguration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessConfiguration createEntity() {
        BusinessConfiguration businessConfiguration = new BusinessConfiguration()
            .businessId(DEFAULT_BUSINESS_ID)
            .discount(DEFAULT_DISCOUNT)
            .shippingCharges(DEFAULT_SHIPPING_CHARGES)
            .tax(DEFAULT_TAX);
        return businessConfiguration;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessConfiguration createUpdatedEntity() {
        BusinessConfiguration businessConfiguration = new BusinessConfiguration()
            .businessId(UPDATED_BUSINESS_ID)
            .discount(UPDATED_DISCOUNT)
            .shippingCharges(UPDATED_SHIPPING_CHARGES)
            .tax(UPDATED_TAX);
        return businessConfiguration;
    }

    @BeforeEach
    public void initTest() {
        businessConfigurationRepository.deleteAll();
        businessConfiguration = createEntity();
    }

    @Test
    void createBusinessConfiguration() throws Exception {
        int databaseSizeBeforeCreate = businessConfigurationRepository.findAll().size();
        // Create the BusinessConfiguration
        restBusinessConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessConfiguration))
            )
            .andExpect(status().isCreated());

        // Validate the BusinessConfiguration in the database
        List<BusinessConfiguration> businessConfigurationList = businessConfigurationRepository.findAll();
        assertThat(businessConfigurationList).hasSize(databaseSizeBeforeCreate + 1);
        BusinessConfiguration testBusinessConfiguration = businessConfigurationList.get(businessConfigurationList.size() - 1);
        assertThat(testBusinessConfiguration.getBusinessId()).isEqualTo(DEFAULT_BUSINESS_ID);
        assertThat(testBusinessConfiguration.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testBusinessConfiguration.getShippingCharges()).isEqualTo(DEFAULT_SHIPPING_CHARGES);
        assertThat(testBusinessConfiguration.getTax()).isEqualTo(DEFAULT_TAX);
    }

    @Test
    void createBusinessConfigurationWithExistingId() throws Exception {
        // Create the BusinessConfiguration with an existing ID
        businessConfiguration.setId("existing_id");

        int databaseSizeBeforeCreate = businessConfigurationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessConfiguration in the database
        List<BusinessConfiguration> businessConfigurationList = businessConfigurationRepository.findAll();
        assertThat(businessConfigurationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkBusinessIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessConfigurationRepository.findAll().size();
        // set the field null
        businessConfiguration.setBusinessId(null);

        // Create the BusinessConfiguration, which fails.

        restBusinessConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessConfiguration))
            )
            .andExpect(status().isBadRequest());

        List<BusinessConfiguration> businessConfigurationList = businessConfigurationRepository.findAll();
        assertThat(businessConfigurationList).hasSize(databaseSizeBeforeTest);
    }

    //    @Test
    //    void getAllBusinessConfigurations() throws Exception {
    //        // Initialize the database
    //        businessConfigurationRepository.save(businessConfiguration);
    //
    //        // Get all the businessConfigurationList
    //        restBusinessConfigurationMockMvc
    //            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
    //            .andExpect(status().isOk())
    //            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
    //            .andExpect(jsonPath("$.[*].id").value(hasItem(businessConfiguration.getId())))
    //            .andExpect(jsonPath("$.[*].businessId").value(hasItem(DEFAULT_BUSINESS_ID)))
    //            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
    //            .andExpect(jsonPath("$.[*].shippingCharges").value(hasItem(DEFAULT_SHIPPING_CHARGES.doubleValue())))
    //            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())));
    //    }

    @Test
    void getBusinessConfiguration() throws Exception {
        // Initialize the database
        businessConfigurationRepository.save(businessConfiguration);

        // Get the businessConfiguration
        restBusinessConfigurationMockMvc
            .perform(get(ENTITY_API_URL_ID, businessConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(businessConfiguration.getId()))
            .andExpect(jsonPath("$.businessId").value(DEFAULT_BUSINESS_ID))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.shippingCharges").value(DEFAULT_SHIPPING_CHARGES.doubleValue()))
            .andExpect(jsonPath("$.tax").value(DEFAULT_TAX.doubleValue()));
    }

    @Test
    void getNonExistingBusinessConfiguration() throws Exception {
        // Get the businessConfiguration
        restBusinessConfigurationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingBusinessConfiguration() throws Exception {
        // Initialize the database
        businessConfigurationRepository.save(businessConfiguration);

        int databaseSizeBeforeUpdate = businessConfigurationRepository.findAll().size();

        // Update the businessConfiguration
        BusinessConfiguration updatedBusinessConfiguration = businessConfigurationRepository
            .findById(businessConfiguration.getId())
            .orElseThrow();
        updatedBusinessConfiguration
            .businessId(UPDATED_BUSINESS_ID)
            .discount(UPDATED_DISCOUNT)
            .shippingCharges(UPDATED_SHIPPING_CHARGES)
            .tax(UPDATED_TAX);

        restBusinessConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBusinessConfiguration.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBusinessConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the BusinessConfiguration in the database
        List<BusinessConfiguration> businessConfigurationList = businessConfigurationRepository.findAll();
        assertThat(businessConfigurationList).hasSize(databaseSizeBeforeUpdate);
        BusinessConfiguration testBusinessConfiguration = businessConfigurationList.get(businessConfigurationList.size() - 1);
        assertThat(testBusinessConfiguration.getBusinessId()).isEqualTo(UPDATED_BUSINESS_ID);
        assertThat(testBusinessConfiguration.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testBusinessConfiguration.getShippingCharges()).isEqualTo(UPDATED_SHIPPING_CHARGES);
        assertThat(testBusinessConfiguration.getTax()).isEqualTo(UPDATED_TAX);
    }

    @Test
    void putNonExistingBusinessConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = businessConfigurationRepository.findAll().size();
        businessConfiguration.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessConfiguration.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessConfiguration in the database
        List<BusinessConfiguration> businessConfigurationList = businessConfigurationRepository.findAll();
        assertThat(businessConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBusinessConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = businessConfigurationRepository.findAll().size();
        businessConfiguration.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessConfiguration in the database
        List<BusinessConfiguration> businessConfigurationList = businessConfigurationRepository.findAll();
        assertThat(businessConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBusinessConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = businessConfigurationRepository.findAll().size();
        businessConfiguration.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessConfiguration))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessConfiguration in the database
        List<BusinessConfiguration> businessConfigurationList = businessConfigurationRepository.findAll();
        assertThat(businessConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBusinessConfigurationWithPatch() throws Exception {
        // Initialize the database
        businessConfigurationRepository.save(businessConfiguration);

        int databaseSizeBeforeUpdate = businessConfigurationRepository.findAll().size();

        // Update the businessConfiguration using partial update
        BusinessConfiguration partialUpdatedBusinessConfiguration = new BusinessConfiguration();
        partialUpdatedBusinessConfiguration.setId(businessConfiguration.getId());

        partialUpdatedBusinessConfiguration.discount(UPDATED_DISCOUNT).shippingCharges(UPDATED_SHIPPING_CHARGES);

        restBusinessConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the BusinessConfiguration in the database
        List<BusinessConfiguration> businessConfigurationList = businessConfigurationRepository.findAll();
        assertThat(businessConfigurationList).hasSize(databaseSizeBeforeUpdate);
        BusinessConfiguration testBusinessConfiguration = businessConfigurationList.get(businessConfigurationList.size() - 1);
        assertThat(testBusinessConfiguration.getBusinessId()).isEqualTo(DEFAULT_BUSINESS_ID);
        assertThat(testBusinessConfiguration.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testBusinessConfiguration.getShippingCharges()).isEqualTo(UPDATED_SHIPPING_CHARGES);
        assertThat(testBusinessConfiguration.getTax()).isEqualTo(DEFAULT_TAX);
    }

    @Test
    void fullUpdateBusinessConfigurationWithPatch() throws Exception {
        // Initialize the database
        businessConfigurationRepository.save(businessConfiguration);

        int databaseSizeBeforeUpdate = businessConfigurationRepository.findAll().size();

        // Update the businessConfiguration using partial update
        BusinessConfiguration partialUpdatedBusinessConfiguration = new BusinessConfiguration();
        partialUpdatedBusinessConfiguration.setId(businessConfiguration.getId());

        partialUpdatedBusinessConfiguration
            .businessId(UPDATED_BUSINESS_ID)
            .discount(UPDATED_DISCOUNT)
            .shippingCharges(UPDATED_SHIPPING_CHARGES)
            .tax(UPDATED_TAX);

        restBusinessConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the BusinessConfiguration in the database
        List<BusinessConfiguration> businessConfigurationList = businessConfigurationRepository.findAll();
        assertThat(businessConfigurationList).hasSize(databaseSizeBeforeUpdate);
        BusinessConfiguration testBusinessConfiguration = businessConfigurationList.get(businessConfigurationList.size() - 1);
        assertThat(testBusinessConfiguration.getBusinessId()).isEqualTo(UPDATED_BUSINESS_ID);
        assertThat(testBusinessConfiguration.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testBusinessConfiguration.getShippingCharges()).isEqualTo(UPDATED_SHIPPING_CHARGES);
        assertThat(testBusinessConfiguration.getTax()).isEqualTo(UPDATED_TAX);
    }

    @Test
    void patchNonExistingBusinessConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = businessConfigurationRepository.findAll().size();
        businessConfiguration.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, businessConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessConfiguration in the database
        List<BusinessConfiguration> businessConfigurationList = businessConfigurationRepository.findAll();
        assertThat(businessConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBusinessConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = businessConfigurationRepository.findAll().size();
        businessConfiguration.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessConfiguration in the database
        List<BusinessConfiguration> businessConfigurationList = businessConfigurationRepository.findAll();
        assertThat(businessConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBusinessConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = businessConfigurationRepository.findAll().size();
        businessConfiguration.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessConfiguration))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessConfiguration in the database
        List<BusinessConfiguration> businessConfigurationList = businessConfigurationRepository.findAll();
        assertThat(businessConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBusinessConfiguration() throws Exception {
        // Initialize the database
        businessConfigurationRepository.save(businessConfiguration);

        int databaseSizeBeforeDelete = businessConfigurationRepository.findAll().size();

        // Delete the businessConfiguration
        restBusinessConfigurationMockMvc
            .perform(delete(ENTITY_API_URL_ID, businessConfiguration.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BusinessConfiguration> businessConfigurationList = businessConfigurationRepository.findAll();
        assertThat(businessConfigurationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
