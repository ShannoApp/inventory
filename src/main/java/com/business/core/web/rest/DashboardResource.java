package com.business.core.web.rest;

import com.business.core.domain.Dashboard;
import com.business.core.repository.DashboardRepository;
import com.business.core.service.DashboardService;
import com.business.core.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.business.core.domain.Dashboard}.
 */
@RestController
@RequestMapping("/api")
public class DashboardResource {

    private final Logger log = LoggerFactory.getLogger(DashboardResource.class);

    private static final String ENTITY_NAME = "dashboard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DashboardService dashboardService;

    private final DashboardRepository dashboardRepository;

    public DashboardResource(DashboardService dashboardService, DashboardRepository dashboardRepository) {
        this.dashboardService = dashboardService;
        this.dashboardRepository = dashboardRepository;
    }

    /**
     * {@code POST  /dashboards} : Create a new dashboard.
     *
     * @param dashboard the dashboard to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dashboard, or with status {@code 400 (Bad Request)} if the dashboard has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dashboards")
    public ResponseEntity<Dashboard> createDashboard(@RequestBody Dashboard dashboard) throws URISyntaxException {
        log.debug("REST request to save Dashboard : {}", dashboard);
        if (dashboard.getId() != null) {
            throw new BadRequestAlertException("A new dashboard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dashboard result = dashboardService.save(dashboard);
        return ResponseEntity
            .created(new URI("/api/dashboards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /dashboards/:id} : Updates an existing dashboard.
     *
     * @param id the id of the dashboard to save.
     * @param dashboard the dashboard to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dashboard,
     * or with status {@code 400 (Bad Request)} if the dashboard is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dashboard couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dashboards/{id}")
    public ResponseEntity<Dashboard> updateDashboard(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Dashboard dashboard
    ) throws URISyntaxException {
        log.debug("REST request to update Dashboard : {}, {}", id, dashboard);
        if (dashboard.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dashboard.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dashboardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Dashboard result = dashboardService.update(dashboard);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dashboard.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /dashboards/:id} : Partial updates given fields of an existing dashboard, field will ignore if it is null
     *
     * @param id the id of the dashboard to save.
     * @param dashboard the dashboard to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dashboard,
     * or with status {@code 400 (Bad Request)} if the dashboard is not valid,
     * or with status {@code 404 (Not Found)} if the dashboard is not found,
     * or with status {@code 500 (Internal Server Error)} if the dashboard couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dashboards/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Dashboard> partialUpdateDashboard(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Dashboard dashboard
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dashboard partially : {}, {}", id, dashboard);
        if (dashboard.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dashboard.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dashboardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dashboard> result = dashboardService.partialUpdate(dashboard);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dashboard.getId())
        );
    }

    /**
     * {@code GET  /dashboards} : get all the dashboards.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dashboards in body.
     */
    @GetMapping("/dashboards")
    public List<Dashboard> getAllDashboards() {
        log.debug("REST request to get all Dashboards");
        return dashboardService.findAll();
    }

    /**
     * {@code GET  /dashboards/:id} : get the "id" dashboard.
     *
     * @param id the id of the dashboard to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dashboard, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dashboards/{id}")
    public ResponseEntity<Dashboard> getDashboard(@PathVariable String id) {
        log.debug("REST request to get Dashboard : {}", id);
        Optional<Dashboard> dashboard = dashboardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dashboard);
    }

    /**
     * {@code DELETE  /dashboards/:id} : delete the "id" dashboard.
     *
     * @param id the id of the dashboard to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dashboards/{id}")
    public ResponseEntity<Void> deleteDashboard(@PathVariable String id) {
        log.debug("REST request to delete Dashboard : {}", id);
        dashboardService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
