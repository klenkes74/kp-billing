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

package de.kaiserpfalzedv.billing.notitia.jpa;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerCreateCommand;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerTO;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerUpdateBillingEmailCommand;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerUpdateContactEmailCommand;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerUpdateCostCenterCommand;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerUpdateNameCommand;
import de.kaiserpfalzedv.billing.notitia.api.customer.EmailAddressTO;
import de.kaiserpfalzedv.billing.notitia.jpa.customer.JPACustomerCreateEvent;
import de.kaiserpfalzedv.billing.notitia.jpa.customer.JPACustomerEvent;
import de.kaiserpfalzedv.billing.notitia.jpa.customer.JPACustomerUpdateBillingEmailEvent;
import de.kaiserpfalzedv.billing.notitia.jpa.customer.JPACustomerUpdateContactEmailEvent;
import de.kaiserpfalzedv.billing.notitia.jpa.customer.JPACustomerUpdateCostCenterEvent;
import de.kaiserpfalzedv.billing.notitia.jpa.customer.JPACustomerUpdateNameEvent;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static org.junit.Assert.assertEquals;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-23
 */
public class CustomerCommandTest {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerCommandTest.class);

    private static final OffsetDateTime EVENT_CREATED = OffsetDateTime.now(ZoneOffset.UTC);
    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private static final String CUSTOMER_NAME = "Customer";
    private static final String COST_CENTER = "12345";
    private static final String CUSTOMER_MAIL_NAME = "Customer Info";
    private static final String CUSTOMER_MAIL_ADDRESS = "info@kaiserpfalz-edv.de";
    private static final UUID CUSTOMER_MAIL_ID = UUID.randomUUID();

    private static final EmailAddressTO EMAIL_ADDRESS = new EmailAddressTO(
            CUSTOMER_MAIL_ID, CUSTOMER_MAIL_NAME, CUSTOMER_MAIL_ADDRESS, "GENERIC"
    );
    private static final HashMap<String, String> TAGS = new HashMap<>();
    private static final CustomerTO CUSTOMER = new CustomerTO();
    static {

        CUSTOMER.setId(CUSTOMER_ID);
        CUSTOMER.setName(CUSTOMER_NAME);
        CUSTOMER.setCostCenter(COST_CENTER);
        CUSTOMER.setBillingAddress(EMAIL_ADDRESS);
        CUSTOMER.setContactAddress(EMAIL_ADDRESS);

        TAGS.put("customer", "12345");
        CUSTOMER.setTags(TAGS);
    }

    @Test
    public void shouldCreateAValidCustomerWhenAllDataIsSpecified() {
        logMethod("create-event", "Created a new customer.");

        UUID eventId = UUID.randomUUID();

        CustomerCreateCommand command = new CustomerCreateCommand(CUSTOMER);
        JPACustomerCreateEvent event = new JPACustomerCreateEvent(command, EVENT_CREATED);

        CustomerTO result = new CustomerTO();

        result = event.update(result);
        LOG.trace("Result: {}", result);

        assertEquals("ID of customer does not match!", CUSTOMER_ID, result.getId());
        assertEquals("Name of customer does not match!", CUSTOMER_NAME, result.getName());
        assertEquals("Cost Center of customer does not match!", COST_CENTER, result.getCostCenter());
        assertEquals("Billing Address does not match!", EMAIL_ADDRESS, result.getBillingAddress());
        assertEquals("Contact Address does not match!", EMAIL_ADDRESS, result.getContactAddress());
    }

    @Test
    public void shouldChangeNameAndCostCenterWhenBothEventAreFired() {
        logMethod("multiple-update-events", "Fire several change events on an existing customer.");

        EmailAddressTO billingAddress = new EmailAddressTO(UUID.randomUUID(), "Customer Fincance", "finance@kaiserpfalz-edv.de", "BILLING");
        EmailAddressTO contactAddress = new EmailAddressTO(UUID.randomUUID(), "Customer Info", "info@kaiserpfalz-edv.de", "CONTACT");

        CustomerCreateCommand createCommand = new CustomerCreateCommand(CUSTOMER);
        CustomerUpdateNameCommand nameUpdateCommand = new CustomerUpdateNameCommand(CUSTOMER_ID, "Neuer Name");
        CustomerUpdateCostCenterCommand costCenterUpdateCommand = new CustomerUpdateCostCenterCommand(CUSTOMER_ID, "54321");
        CustomerUpdateBillingEmailCommand billingEmailUpdateCommand = new CustomerUpdateBillingEmailCommand(CUSTOMER_ID, billingAddress);
        CustomerUpdateContactEmailCommand contactEmailUpdateCommand = new CustomerUpdateContactEmailCommand(CUSTOMER_ID, contactAddress);

        JPACustomerCreateEvent createEvent = new JPACustomerCreateEvent(createCommand, EVENT_CREATED);
        JPACustomerUpdateNameEvent updateNameEvent = new JPACustomerUpdateNameEvent(nameUpdateCommand, EVENT_CREATED);
        JPACustomerUpdateCostCenterEvent updateCostCenterEvent = new JPACustomerUpdateCostCenterEvent(costCenterUpdateCommand, EVENT_CREATED);
        JPACustomerUpdateBillingEmailEvent updateBillingEmailEvent = new JPACustomerUpdateBillingEmailEvent(billingEmailUpdateCommand, EVENT_CREATED);
        JPACustomerUpdateContactEmailEvent updateContactEmailEvent = new JPACustomerUpdateContactEmailEvent(contactEmailUpdateCommand, EVENT_CREATED);

        List<JPACustomerEvent> commands = new ArrayList<>(5);
        Collections.addAll(commands, createEvent, updateNameEvent, updateCostCenterEvent, updateBillingEmailEvent, updateContactEmailEvent);

        CustomerTO result = new CustomerTO();
        for(JPACustomerEvent event : commands) {
            result = event.update(result);
        }
        LOG.trace("Result: {}", result);

        assertEquals("ID of customer does not match!", CUSTOMER_ID, result.getId());
        assertEquals("Name should be 'Neuer Name'", "Neuer Name", result.getName());
        assertEquals("Cost center should be '54321'", "54321", result.getCostCenter());
        assertEquals("Billing Address does not match!", billingAddress, result.getBillingAddress());
        assertEquals("Contact Address does not match!", contactAddress, result.getContactAddress());
    }



    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @BeforeClass
    public static void setUpMDC() {
        MDC.put("test", "Customer Events");

        LOG.info("===[{}]========[BEGIN]===", MDC.get("test"));
    }

    @AfterClass
    public static void tearDownMDC() {
        LOG.info("===[{}]==========[END]===", MDC.get("test"));
        MDC.remove("id");
        MDC.remove("test");
    }

}
