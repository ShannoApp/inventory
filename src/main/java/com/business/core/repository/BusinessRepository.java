package com.business.core.repository;

import com.business.core.domain.Business;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Business entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusinessRepository extends MongoRepository<Business, String> {
    List<Business> findByUsersContaining(String userId);
}
