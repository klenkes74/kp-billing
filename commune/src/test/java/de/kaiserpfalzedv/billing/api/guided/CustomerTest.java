/*
 *    Copyright 2018 Kaiserpfalz EDV-Service, Roland T. Lichti
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.kaiserpfalzedv.billing.api.guided;

import java.util.UUID;

import javax.xml.registry.JAXRException;

import de.kaiserpfalzedv.billing.api.common.EmailAddress;
import de.kaiserpfalzedv.billing.api.common.impl.EmailAddressBuilder;
import de.kaiserpfalzedv.billing.api.common.impl.NullEmailAddress;
import de.kaiserpfalzedv.billing.api.guided.impl.CustomerBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.bridge.SLF4JBridgeHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class CustomerTest {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerTest.class);

    private static final UUID ID = UUID.randomUUID();
    private static final String NAME = "Customer Name";
    private static final String COST_REFERENCE = "C-123456789";
    private static final EmailAddress CONTACT_ADDRESS = new EmailAddressBuilder()
            .setAddress("unit-test-contact@email.mail")
            .contactAddress()
            .build();
    private static final EmailAddress BILLING_ADDRESS = new EmailAddressBuilder()
            .setAddress("unit-test-billing@email.mail")
            .billingAddress()
            .build();

    private static final Customer CUSTOMER = new CustomerBuilder()
            .setId(ID)
            .setName(NAME)
            .setCostReference(COST_REFERENCE)
            .setContactAddress(CONTACT_ADDRESS)
            .setBillingAddress(BILLING_ADDRESS)
            .build();


    private CustomerBuilder service;

    @BeforeClass
    public static void setUpClass() {
        MDC.put("test", "CustomerTest");

        if (!SLF4JBridgeHandler.isInstalled()) {
            SLF4JBridgeHandler.install();
        }
    }

    @AfterClass
    public static void tearDownClass() {
        MDC.remove("test");
        MDC.remove("id");
    }

    @Test
    public void generateSimpleCustomer() throws JAXRException {
        logMethod("customer", "Testing a simple customer");

        Customer result = service
                .setName(NAME)
                .setCostReference(COST_REFERENCE)
                .build();
        LOG.debug("result: {}", result);

        assertNotNull("The id should default to a random UUID", result.getId());
        assertEquals("Name does not match", NAME, result.getName());
        assertEquals("CostReference does not match", COST_REFERENCE, result.getCostReference());
        assertEquals("Contact Address does not match", NullEmailAddress.INSTANCE, result.getContactAddress());
        assertEquals("Billing Address does not match", NullEmailAddress.INSTANCE, result.getBillingAddress());
    }

    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @Test
    public void copyCustomer() throws JAXRException {
        logMethod("copy-customer", "Copying the customer: {}", CUSTOMER);

        Customer result = service.copy(CUSTOMER).build();
        LOG.debug("result: {}", result);

        assertEquals("ID does not match", ID, result.getId());
        assertEquals("Name does not match", NAME, result.getName());
        assertEquals("CostReference does not match", COST_REFERENCE, result.getCostReference());
        assertEquals("Contact Address does not match", CONTACT_ADDRESS, result.getContactAddress());
        assertEquals("Billing Address does not match", BILLING_ADDRESS, result.getBillingAddress());
    }

    @Test(timeout = 80L)
    public void runtimeTest() {
        logMethod("runtime-verification", "checking the runtime of the tarif builder ...");

        for (int i = 0; i < 1000; i++) {
            service
                    .setName(NAME)
                    .setCostReference(COST_REFERENCE)
                    .build();
        }
    }

    @Before
    public void setUpService() {
        service = new CustomerBuilder();
    }

    @After
    public void tearDownService() {
        MDC.remove("id");
    }
}
