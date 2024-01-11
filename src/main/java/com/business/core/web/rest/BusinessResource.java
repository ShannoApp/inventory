package com.business.core.web.rest;

import com.business.core.domain.Business;
import com.business.core.repository.BusinessRepository;
import com.business.core.security.SecurityUtils;
import com.business.core.service.BusinessService;
import com.business.core.service.UserService;
import com.business.core.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
 * REST controller for managing {@link com.business.core.domain.Business}.
 */
@RestController
@RequestMapping("/api")
public class BusinessResource {

    private final Logger log = LoggerFactory.getLogger(BusinessResource.class);

    private static final String ENTITY_NAME = "business";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusinessService businessService;

    private final BusinessRepository businessRepository;

    public BusinessResource(BusinessService businessService, BusinessRepository businessRepository) {
        this.businessService = businessService;
        this.businessRepository = businessRepository;
    }

    /**
     * {@code POST  /businesses} : Create a new business.
     *
     * @param business the business to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new business, or with status {@code 400 (Bad Request)} if
     *         the business has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/businesses")
    public ResponseEntity<Business> createBusiness(@Valid @RequestBody Business business) throws URISyntaxException {
        log.debug("REST request to save Business : {}", business);

        if (business.getId() != null) {
            throw new BadRequestAlertException("A new business cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Business result = businessService.save(business);
        return ResponseEntity
            .created(new URI("/api/businesses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /businesses/:id} : Updates an existing business.
     *
     * @param id       the id of the business to save.
     * @param business the business to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated business, or with status {@code 400 (Bad Request)} if the
     *         business is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the business couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/businesses/{id}")
    public ResponseEntity<Business> updateBusiness(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Business business
    ) throws URISyntaxException {
        log.debug("REST request to update Business : {}, {}", id, business);
        if (business.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, business.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Business result = businessService.update(business);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, business.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /businesses/:id} : Partial updates given fields of an existing
     * business, field will ignore if it is null
     *
     * @param id       the id of the business to save.
     * @param business the business to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated business, or with status {@code 400 (Bad Request)} if the
     *         business is not valid, or with status {@code 404 (Not Found)} if the
     *         business is not found, or with status
     *         {@code 500 (Internal Server Error)} if the business couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/businesses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Business> partialUpdateBusiness(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Business business
    ) throws URISyntaxException {
        log.debug("REST request to partial update Business partially : {}, {}", id, business);
        if (business.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, business.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Business> result = businessService.partialUpdate(business);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, business.getId())
        );
    }

    /**
     * {@code GET  /businesses} : get all the businesses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of businesses in body.
     */
    @GetMapping("/businesses")
    public List<Business> getAllBusinesses() {
        log.debug("REST request to get all Businesses");
        return businessService.findAll();
    }

    /**
     * {@code GET  /businesses/:id} : get the "id" business.
     *
     * @param id the id of the business to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the business, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/businesses/{id}")
    public ResponseEntity<Business> getBusiness(@PathVariable String id) {
        log.debug("REST request to get Business : {}", id);
        Optional<Business> business = businessService.findOne(id);
        return ResponseUtil.wrapOrNotFound(business);
    }

    /**
     * {@code DELETE  /businesses/:id} : delete the "id" business.
     *
     * @param id the id of the business to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/businesses/{id}")
    public ResponseEntity<Void> deleteBusiness(@PathVariable String id) {
        log.debug("REST request to delete Business : {}", id);
        businessService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
