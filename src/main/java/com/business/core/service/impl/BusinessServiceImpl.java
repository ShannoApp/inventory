package com.business.core.service.impl;

import com.business.core.domain.Business;
import com.business.core.domain.Dashboard;
import com.business.core.domain.User;
import com.business.core.repository.BusinessRepository;
import com.business.core.repository.DashboardRepository;
import com.business.core.repository.UserRepository;
import com.business.core.security.SecurityUtils;
import com.business.core.service.BusinessService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.business.core.domain.Business}.
 */
@Service
public class BusinessServiceImpl implements BusinessService {

    private final Logger log = LoggerFactory.getLogger(BusinessServiceImpl.class);

    private final BusinessRepository businessRepository;

    private UserRepository userRepository;

    private DashboardRepository dashRepository;

    public BusinessServiceImpl(BusinessRepository businessRepository, UserRepository userRepository, DashboardRepository dashRepository) {
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
        this.dashRepository = dashRepository;
    }

    @Override
    public Business save(Business business) {
        log.debug("Request to save Business : {}", business);
        Set<String> users = business.getUsers();
        String userName = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new UsernameNotFoundException("User not found"));
        log.debug("Username : {}", userName);
        users.add(userName);
        business.setUsers(users);

        Dashboard dashboard = new Dashboard();
        Business business2 = businessRepository.save(business);

        dashboard.setBusinessId(business2.getId());

        dashRepository.save(dashboard);

        return business2;
    }

    @Override
    public Business update(Business business) {
        log.debug("Request to update Business : {}", business);
        return businessRepository.save(business);
    }

    @Override
    public Optional<Business> partialUpdate(Business business) {
        log.debug("Request to partially update Business : {}", business);

        return businessRepository
            .findById(business.getId())
            .map(existingBusiness -> {
                if (business.getName() != null) {
                    existingBusiness.setName(business.getName());
                }
                if (business.getWebsite() != null) {
                    existingBusiness.setWebsite(business.getWebsite());
                }
                if (business.getEmail() != null) {
                    existingBusiness.setEmail(business.getEmail());
                }
                if (business.getPhone() != null) {
                    existingBusiness.setPhone(business.getPhone());
                }

                return existingBusiness;
            })
            .map(businessRepository::save);
    }

    @Override
    public List<Business> findAll() {
        log.debug("Request to get all Businesses");
        String userName = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return businessRepository.findByUsersContaining(userName);
    }

    @Override
    public Optional<Business> findOne(String id) {
        log.debug("Request to get Business : {}", id);
        return businessRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Business : {}", id);
        businessRepository.deleteById(id);
    }
}
