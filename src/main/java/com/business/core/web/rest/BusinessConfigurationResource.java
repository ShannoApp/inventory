package com.business.core.web.rest;

import com.business.core.domain.BusinessConfiguration;
import com.business.core.repository.BusinessConfigurationRepository;
import com.business.core.service.BusinessConfigurationService;
import com.business.core.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.business.core.domain.BusinessConfiguration}.
 */
@RestController
@RequestMapping("/api")
public class BusinessConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(BusinessConfigurationResource.class);

    private static final String ENTITY_NAME = "businessConfiguration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusinessConfigurationService businessConfigurationService;

    private final BusinessConfigurationRepository businessConfigurationRepository;

    public BusinessConfigurationResource(
        BusinessConfigurationService businessConfigurationService,
        BusinessConfigurationRepository businessConfigurationRepository
    ) {
        this.businessConfigurationService = businessConfigurationService;
        this.businessConfigurationRepository = businessConfigurationRepository;
    }

    /**
     * {@code POST  /business-configurations} : Create a new businessConfiguration.
     *
     * @param businessConfiguration the businessConfiguration to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new businessConfiguration, or with status {@code 400 (Bad Request)} if the businessConfiguration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/business-configurations")
    public ResponseEntity<BusinessConfiguration> createBusinessConfiguration(
        @Valid @RequestBody BusinessConfiguration businessConfiguration
    ) throws URISyntaxException {
        log.debug("REST request to save BusinessConfiguration : {}", businessConfiguration);
        if (businessConfiguration.getId() != null) {
            throw new BadRequestAlertException("A new businessConfiguration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusinessConfiguration result = businessConfigurationService.save(businessConfiguration);
        return ResponseEntity
            .created(new URI("/api/business-configurations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /business-configurations/:id} : Updates an existing businessConfiguration.
     *
     * @param id the id of the businessConfiguration to save.
     * @param businessConfiguration the businessConfiguration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessConfiguration,
     * or with status {@code 400 (Bad Request)} if the businessConfiguration is not valid,
     * or with status {@code 500 (Internal Server Error)} if the businessConfiguration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/business-configurations/{id}")
    public ResponseEntity<BusinessConfiguration> updateBusinessConfiguration(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody BusinessConfiguration businessConfiguration
    ) throws URISyntaxException {
        log.debug("REST request to update BusinessConfiguration : {}, {}", id, businessConfiguration);
        if (businessConfiguration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessConfiguration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BusinessConfiguration result = businessConfigurationService.update(businessConfiguration);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, businessConfiguration.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /business-configurations/:id} : Partial updates given fields of an existing businessConfiguration, field will ignore if it is null
     *
     * @param id the id of the businessConfiguration to save.
     * @param businessConfiguration the businessConfiguration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessConfiguration,
     * or with status {@code 400 (Bad Request)} if the businessConfiguration is not valid,
     * or with status {@code 404 (Not Found)} if the businessConfiguration is not found,
     * or with status {@code 500 (Internal Server Error)} if the businessConfiguration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/business-configurations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BusinessConfiguration> partialUpdateBusinessConfiguration(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody BusinessConfiguration businessConfiguration
    ) throws URISyntaxException {
        log.debug("REST request to partial update BusinessConfiguration partially : {}, {}", id, businessConfiguration);
        if (businessConfiguration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessConfiguration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusinessConfiguration> result = businessConfigurationService.partialUpdate(businessConfiguration);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, businessConfiguration.getId())
        );
    }

    /**
     * {@code GET  /business-configurations} : get all the businessConfigurations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of businessConfigurations in body.
     */
    @GetMapping("{businessId}/business-configurations")
    public List<BusinessConfiguration> getAllBusinessConfigurations(@PathVariable String businessId) {
        log.debug("REST request to get all BusinessConfigurations");
        return businessConfigurationService.findByBusinessId(businessId);
    }

    /**
     * {@code GET  /business-configurations/:id} : get the "id" businessConfiguration.
     *
     * @param id the id of the businessConfiguration to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the businessConfiguration, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/business-configurations/{id}")
    public ResponseEntity<BusinessConfiguration> getBusinessConfiguration(@PathVariable String id) {
        log.debug("REST request to get BusinessConfiguration : {}", id);
        Optional<BusinessConfiguration> businessConfiguration = businessConfigurationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(businessConfiguration);
    }

    /**
     * {@code DELETE  /business-configurations/:id} : delete the "id" businessConfiguration.
     *
     * @param id the id of the businessConfiguration to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/business-configurations/{id}")
    public ResponseEntity<Void> deleteBusinessConfiguration(@PathVariable String id) {
        log.debug("REST request to delete BusinessConfiguration : {}", id);
        businessConfigurationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
