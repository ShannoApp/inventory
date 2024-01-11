package com.business.core.service;

import com.business.core.domain.Category;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.business.core.domain.Category}.
 */
public interface CategoryService {
    /**
     * Save a category.
     *
     * @param category the entity to save.
     * @return the persisted entity.
     */
    Category save(Category category);

    /**
     * Updates a category.
     *
     * @param category the entity to update.
     * @return the persisted entity.
     */
    Category update(Category category);

    /**
     * Partially updates a category.
     *
     * @param category the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Category> partialUpdate(Category category);

    /**
     * Get all the categories.
     *
     * @return the list of entities.
     */
    List<Category> findAll(String businessId);

    /**
     * Get the "id" category.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Category> findOne(String id);

    /**
     * Delete the "id" category.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
