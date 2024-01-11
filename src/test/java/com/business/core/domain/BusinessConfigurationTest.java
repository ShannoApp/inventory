package com.business.core.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.business.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BusinessConfigurationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessConfiguration.class);
        BusinessConfiguration businessConfiguration1 = new BusinessConfiguration();
        businessConfiguration1.setId("id1");
        BusinessConfiguration businessConfiguration2 = new BusinessConfiguration();
        businessConfiguration2.setId(businessConfiguration1.getId());
        assertThat(businessConfiguration1).isEqualTo(businessConfiguration2);
        businessConfiguration2.setId("id2");
        assertThat(businessConfiguration1).isNotEqualTo(businessConfiguration2);
        businessConfiguration1.setId(null);
        assertThat(businessConfiguration1).isNotEqualTo(businessConfiguration2);
    }
}
