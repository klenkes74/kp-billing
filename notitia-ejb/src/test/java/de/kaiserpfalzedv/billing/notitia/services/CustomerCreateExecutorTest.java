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

package de.kaiserpfalzedv.billing.notitia.services;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import de.kaiserpfalzedv.billing.notitia.api.commands.CommandFailedException;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerCreateCommand;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerTO;
import de.kaiserpfalzedv.billing.notitia.api.customer.EmailAddressTO;
import de.kaiserpfalzedv.billing.notitia.jpa.customer.JPACustomerCreateEvent;
import de.kaiserpfalzedv.billing.notitia.services.customer.CustomerCreateExecutor;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-25
 */
public class CustomerCreateExecutorTest {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerCreateExecutorTest.class);

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


    private CustomerCreateExecutor service;

    @Mock
    private EntityManager em;
    @Mock
    private EntityTransaction tx;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Test
    public void shouldCreateCustomerWhenEventListenerIsCalled() throws CommandFailedException {
        logMethod("simple-call", "Creating customer: {}", CUSTOMER);

        CustomerCreateCommand command = new CustomerCreateCommand(CUSTOMER);

        service.execute(command);
    }

    @Test(expected = CommandFailedException.class)
    public void shouldThrowCommandExceptionWhenPersistenceExceptionIsThrownOutsideTransaction() throws CommandFailedException {
        logMethod("persistence-failure-outside-transaction", "Creating customer for failing outside of transaction: {}", CUSTOMER);

        CustomerCreateCommand command = new CustomerCreateCommand(CUSTOMER);

        doThrow(new PersistenceException()).when(em).persist(any(JPACustomerCreateEvent.class));
        when(em.isJoinedToTransaction()).thenReturn(false);
        
        service.execute(command);
    }


    @Test(expected = CommandFailedException.class)
    public void shouldThrowCommandExceptionWhenPersistenceExceptionIsThrownInsideTransaction() throws CommandFailedException {
        logMethod("persistence-failure-inside-transaction", "Creating customer for failing inside transaction: {}", CUSTOMER);

        CustomerCreateCommand command = new CustomerCreateCommand(CUSTOMER);

        doThrow(new PersistenceException()).when(em).persist(any(JPACustomerCreateEvent.class));
        when(em.isJoinedToTransaction()).thenReturn(true);

        service.execute(command);

        verify(tx, atLeastOnce()).setRollbackOnly();
    }

    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @Before
    public void setUp() {
        service = new CustomerCreateExecutor();
        service.setEntityManager(em);

        when(em.getTransaction()).thenReturn(tx);
    }

    @BeforeClass
    public static void setUpMDC() {
        MDC.put("test", "Create Customer Executor");

        LOG.info("===[{}]========[BEGIN]===", MDC.get("test"));
    }

    @AfterClass
    public static void tearDownMDC() {
        LOG.info("===[{}]==========[END]===", MDC.get("test"));
        MDC.remove("id");
        MDC.remove("test");
    }
}
