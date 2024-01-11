package com.business.core.repository;

import com.business.core.domain.Dashboard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Dashboard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DashboardRepository extends MongoRepository<Dashboard, String> {
    public Dashboard findByBusinessId(String businessId);
}
