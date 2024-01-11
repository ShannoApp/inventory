package com.business.core.service;

import com.business.core.domain.BusinessConfiguration;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.business.core.domain.BusinessConfiguration}.
 */
public interface BusinessConfigurationService {
    /**
     * Save a businessConfiguration.
     *
     * @param businessConfiguration the entity to save.
     * @return the persisted entity.
     */
    BusinessConfiguration save(BusinessConfiguration businessConfiguration);

    /**
     * Updates a businessConfiguration.
     *
     * @param businessConfiguration the entity to update.
     * @return the persisted entity.
     */
    BusinessConfiguration update(BusinessConfiguration businessConfiguration);

    /**
     * Partially updates a businessConfiguration.
     *
     * @param businessConfiguration the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BusinessConfiguration> partialUpdate(BusinessConfiguration businessConfiguration);

    /**
     * Get all the businessConfigurations.
     *
     * @return the list of entities.
     */
    List<BusinessConfiguration> findAll();

    /**
     * Get the "id" businessConfiguration.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BusinessConfiguration> findOne(String id);

    /**
     * Delete the "id" businessConfiguration.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    List<BusinessConfiguration> findByBusinessId(String businessId);
}
