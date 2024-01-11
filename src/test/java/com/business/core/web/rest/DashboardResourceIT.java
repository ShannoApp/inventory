package com.business.core.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.business.core.IntegrationTest;
import com.business.core.domain.Dashboard;
import com.business.core.repository.DashboardRepository;
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
 * Integration tests for the {@link DashboardResource} REST controller.
 */
@SpringBootTest
@SpringJUnitConfig
@AutoConfigureMockMvc
@WithMockUser
class DashboardResourceIT {

    private static final Double DEFAULT_TOTAL_SALE_TILL_DATE = 1D;
    private static final Double UPDATED_TOTAL_SALE_TILL_DATE = 2D;

    private static final Double DEFAULT_TOTAL_PROFIT_TILL_DATE = 1D;
    private static final Double UPDATED_TOTAL_PROFIT_TILL_DATE = 2D;

    private static final String ENTITY_API_URL = "/api/dashboards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DashboardRepository dashboardRepository;

    @Autowired
    private MockMvc restDashboardMockMvc;

    private Dashboard dashboard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dashboard createEntity() {
        Dashboard dashboard = new Dashboard()
            .totalSaleTillDate(DEFAULT_TOTAL_SALE_TILL_DATE)
            .totalProfitTillDate(DEFAULT_TOTAL_PROFIT_TILL_DATE);
        return dashboard;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dashboard createUpdatedEntity() {
        Dashboard dashboard = new Dashboard()
            .totalSaleTillDate(UPDATED_TOTAL_SALE_TILL_DATE)
            .totalProfitTillDate(UPDATED_TOTAL_PROFIT_TILL_DATE);
        return dashboard;
    }

    @BeforeEach
    public void initTest() {
        dashboardRepository.deleteAll();
        dashboard = createEntity();
    }

    @Test
    void createDashboard() throws Exception {
        int databaseSizeBeforeCreate = dashboardRepository.findAll().size();
        // Create the Dashboard
        restDashboardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dashboard)))
            .andExpect(status().isCreated());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeCreate + 1);
        Dashboard testDashboard = dashboardList.get(dashboardList.size() - 1);
        assertThat(testDashboard.getTotalSaleTillDate()).isEqualTo(DEFAULT_TOTAL_SALE_TILL_DATE);
        assertThat(testDashboard.getTotalProfitTillDate()).isEqualTo(DEFAULT_TOTAL_PROFIT_TILL_DATE);
    }

    @Test
    void createDashboardWithExistingId() throws Exception {
        // Create the Dashboard with an existing ID
        dashboard.setId("existing_id");

        int databaseSizeBeforeCreate = dashboardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDashboardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dashboard)))
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDashboards() throws Exception {
        // Initialize the database
        dashboardRepository.save(dashboard);

        // Get all the dashboardList
        restDashboardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dashboard.getId())))
            .andExpect(jsonPath("$.[*].totalSaleTillDate").value(hasItem(DEFAULT_TOTAL_SALE_TILL_DATE.doubleValue())))
            .andExpect(jsonPath("$.[*].totalProfitTillDate").value(hasItem(DEFAULT_TOTAL_PROFIT_TILL_DATE.doubleValue())));
    }

    @Test
    void getDashboard() throws Exception {
        // Initialize the database
        dashboardRepository.save(dashboard);

        // Get the dashboard
        restDashboardMockMvc
            .perform(get(ENTITY_API_URL_ID, dashboard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dashboard.getId()))
            .andExpect(jsonPath("$.totalSaleTillDate").value(DEFAULT_TOTAL_SALE_TILL_DATE.doubleValue()))
            .andExpect(jsonPath("$.totalProfitTillDate").value(DEFAULT_TOTAL_PROFIT_TILL_DATE.doubleValue()));
    }

    @Test
    void getNonExistingDashboard() throws Exception {
        // Get the dashboard
        restDashboardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingDashboard() throws Exception {
        // Initialize the database
        dashboardRepository.save(dashboard);

        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();

        // Update the dashboard
        Dashboard updatedDashboard = dashboardRepository.findById(dashboard.getId()).orElseThrow();
        updatedDashboard.totalSaleTillDate(UPDATED_TOTAL_SALE_TILL_DATE).totalProfitTillDate(UPDATED_TOTAL_PROFIT_TILL_DATE);

        restDashboardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDashboard.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDashboard))
            )
            .andExpect(status().isOk());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
        Dashboard testDashboard = dashboardList.get(dashboardList.size() - 1);
        assertThat(testDashboard.getTotalSaleTillDate()).isEqualTo(UPDATED_TOTAL_SALE_TILL_DATE);
        assertThat(testDashboard.getTotalProfitTillDate()).isEqualTo(UPDATED_TOTAL_PROFIT_TILL_DATE);
    }

    @Test
    void putNonExistingDashboard() throws Exception {
        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();
        dashboard.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dashboard.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dashboard))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDashboard() throws Exception {
        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();
        dashboard.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dashboard))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDashboard() throws Exception {
        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();
        dashboard.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dashboard)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDashboardWithPatch() throws Exception {
        // Initialize the database
        dashboardRepository.save(dashboard);

        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();

        // Update the dashboard using partial update
        Dashboard partialUpdatedDashboard = new Dashboard();
        partialUpdatedDashboard.setId(dashboard.getId());

        partialUpdatedDashboard.totalProfitTillDate(UPDATED_TOTAL_PROFIT_TILL_DATE);

        restDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDashboard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDashboard))
            )
            .andExpect(status().isOk());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
        Dashboard testDashboard = dashboardList.get(dashboardList.size() - 1);
        assertThat(testDashboard.getTotalSaleTillDate()).isEqualTo(DEFAULT_TOTAL_SALE_TILL_DATE);
        assertThat(testDashboard.getTotalProfitTillDate()).isEqualTo(UPDATED_TOTAL_PROFIT_TILL_DATE);
    }

    @Test
    void fullUpdateDashboardWithPatch() throws Exception {
        // Initialize the database
        dashboardRepository.save(dashboard);

        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();

        // Update the dashboard using partial update
        Dashboard partialUpdatedDashboard = new Dashboard();
        partialUpdatedDashboard.setId(dashboard.getId());

        partialUpdatedDashboard.totalSaleTillDate(UPDATED_TOTAL_SALE_TILL_DATE).totalProfitTillDate(UPDATED_TOTAL_PROFIT_TILL_DATE);

        restDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDashboard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDashboard))
            )
            .andExpect(status().isOk());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
        Dashboard testDashboard = dashboardList.get(dashboardList.size() - 1);
        assertThat(testDashboard.getTotalSaleTillDate()).isEqualTo(UPDATED_TOTAL_SALE_TILL_DATE);
        assertThat(testDashboard.getTotalProfitTillDate()).isEqualTo(UPDATED_TOTAL_PROFIT_TILL_DATE);
    }

    @Test
    void patchNonExistingDashboard() throws Exception {
        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();
        dashboard.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dashboard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dashboard))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDashboard() throws Exception {
        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();
        dashboard.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dashboard))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDashboard() throws Exception {
        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();
        dashboard.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dashboard))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDashboard() throws Exception {
        // Initialize the database
        dashboardRepository.save(dashboard);

        int databaseSizeBeforeDelete = dashboardRepository.findAll().size();

        // Delete the dashboard
        restDashboardMockMvc
            .perform(delete(ENTITY_API_URL_ID, dashboard.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
