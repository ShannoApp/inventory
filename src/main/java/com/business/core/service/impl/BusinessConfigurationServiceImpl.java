package com.business.core.service.impl;

import com.business.core.domain.BusinessConfiguration;
import com.business.core.repository.BusinessConfigurationRepository;
import com.business.core.service.BusinessConfigurationService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.business.core.domain.BusinessConfiguration}.
 */
@Service
public class BusinessConfigurationServiceImpl implements BusinessConfigurationService {

    private final Logger log = LoggerFactory.getLogger(BusinessConfigurationServiceImpl.class);

    private final BusinessConfigurationRepository businessConfigurationRepository;

    public BusinessConfigurationServiceImpl(BusinessConfigurationRepository businessConfigurationRepository) {
        this.businessConfigurationRepository = businessConfigurationRepository;
    }

    @Override
    public BusinessConfiguration save(BusinessConfiguration businessConfiguration) {
        log.debug("Request to save BusinessConfiguration : {}", businessConfiguration);
        return businessConfigurationRepository.save(businessConfiguration);
    }

    @Override
    public BusinessConfiguration update(BusinessConfiguration businessConfiguration) {
        log.debug("Request to update BusinessConfiguration : {}", businessConfiguration);
        return businessConfigurationRepository.save(businessConfiguration);
    }

    @Override
    public Optional<BusinessConfiguration> partialUpdate(BusinessConfiguration businessConfiguration) {
        log.debug("Request to partially update BusinessConfiguration : {}", businessConfiguration);

        return businessConfigurationRepository
            .findById(businessConfiguration.getId())
            .map(existingBusinessConfiguration -> {
                if (businessConfiguration.getBusinessId() != null) {
                    existingBusinessConfiguration.setBusinessId(businessConfiguration.getBusinessId());
                }
                if (businessConfiguration.getDiscount() != null) {
                    existingBusinessConfiguration.setDiscount(businessConfiguration.getDiscount());
                }
                if (businessConfiguration.getShippingCharges() != null) {
                    existingBusinessConfiguration.setShippingCharges(businessConfiguration.getShippingCharges());
                }
                if (businessConfiguration.getTax() != null) {
                    existingBusinessConfiguration.setTax(businessConfiguration.getTax());
                }

                return existingBusinessConfiguration;
            })
            .map(businessConfigurationRepository::save);
    }

    @Override
    public List<BusinessConfiguration> findAll() {
        log.debug("Request to get all BusinessConfigurations");
        return businessConfigurationRepository.findAll();
    }

    @Override
    public Optional<BusinessConfiguration> findOne(String id) {
        log.debug("Request to get BusinessConfiguration : {}", id);
        return businessConfigurationRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete BusinessConfiguration : {}", id);
        businessConfigurationRepository.deleteById(id);
    }

    @Override
    public List<BusinessConfiguration> findByBusinessId(String businessId) {
        // TODO Auto-generated method stub
        return this.businessConfigurationRepository.findByBusinessId(businessId);
    }
}
