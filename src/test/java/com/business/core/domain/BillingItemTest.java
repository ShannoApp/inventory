package com.business.core.domain;

import com.business.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillingItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillingItem.class);
    }
}
