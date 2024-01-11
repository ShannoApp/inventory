package com.business.core.service;

import com.business.core.domain.Business;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.business.core.domain.Business}.
 */
public interface BusinessService {
    /**
     * Save a business.
     *
     * @param business the entity to save.
     * @return the persisted entity.
     */
    Business save(Business business);

    /**
     * Updates a business.
     *
     * @param business the entity to update.
     * @return the persisted entity.
     */
    Business update(Business business);

    /**
     * Partially updates a business.
     *
     * @param business the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Business> partialUpdate(Business business);

    /**
     * Get all the businesses.
     *
     * @return the list of entities.
     */
    List<Business> findAll();

    /**
     * Get the "id" business.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Business> findOne(String id);

    /**
     * Delete the "id" business.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
