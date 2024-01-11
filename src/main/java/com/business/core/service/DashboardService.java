package com.business.core.service;

import com.business.core.domain.Dashboard;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.business.core.domain.Dashboard}.
 */
public interface DashboardService {
    /**
     * Save a dashboard.
     *
     * @param dashboard the entity to save.
     * @return the persisted entity.
     */
    Dashboard save(Dashboard dashboard);

    /**
     * Updates a dashboard.
     *
     * @param dashboard the entity to update.
     * @return the persisted entity.
     */
    Dashboard update(Dashboard dashboard);

    /**
     * Partially updates a dashboard.
     *
     * @param dashboard the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Dashboard> partialUpdate(Dashboard dashboard);

    /**
     * Get all the dashboards.
     *
     * @return the list of entities.
     */
    List<Dashboard> findAll();

    /**
     * Get the "id" dashboard.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Dashboard> findOne(String id);

    /**
     * Delete the "id" dashboard.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
