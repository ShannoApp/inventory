package com.business.core.service.impl;

import com.business.core.domain.Dashboard;
import com.business.core.repository.DashboardRepository;
import com.business.core.service.DashboardService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.business.core.domain.Dashboard}.
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    private final Logger log = LoggerFactory.getLogger(DashboardServiceImpl.class);

    private final DashboardRepository dashboardRepository;

    public DashboardServiceImpl(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    @Override
    public Dashboard save(Dashboard dashboard) {
        log.debug("Request to save Dashboard : {}", dashboard);
        return dashboardRepository.save(dashboard);
    }

    @Override
    public Dashboard update(Dashboard dashboard) {
        log.debug("Request to update Dashboard : {}", dashboard);
        return dashboardRepository.save(dashboard);
    }

    @Override
    public Optional<Dashboard> partialUpdate(Dashboard dashboard) {
        log.debug("Request to partially update Dashboard : {}", dashboard);

        return dashboardRepository
            .findById(dashboard.getId())
            .map(existingDashboard -> {
                if (dashboard.getTotalSaleTillDate() != null) {
                    existingDashboard.setTotalSaleTillDate(dashboard.getTotalSaleTillDate());
                }
                if (dashboard.getTotalProfitTillDate() != null) {
                    existingDashboard.setTotalProfitTillDate(dashboard.getTotalProfitTillDate());
                }

                return existingDashboard;
            })
            .map(dashboardRepository::save);
    }

    @Override
    public List<Dashboard> findAll() {
        log.debug("Request to get all Dashboards");
        return dashboardRepository.findAll();
    }

    @Override
    public Optional<Dashboard> findOne(String id) {
        log.debug("Request to get Dashboard : {}", id);
        return dashboardRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Dashboard : {}", id);
        dashboardRepository.deleteById(id);
    }
}
