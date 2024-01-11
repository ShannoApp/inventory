package com.business.core.repository;

import com.business.core.domain.BusinessConfiguration;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the BusinessConfiguration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusinessConfigurationRepository extends MongoRepository<BusinessConfiguration, String> {
    List<BusinessConfiguration> findByBusinessId(String businessId);
}
