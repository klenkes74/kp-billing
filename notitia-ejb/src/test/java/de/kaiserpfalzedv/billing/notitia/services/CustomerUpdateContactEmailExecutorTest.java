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

import java.util.HashMap;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import de.kaiserpfalzedv.billing.notitia.api.commands.CommandFailedException;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerUpdateContactEmailCommand;
import de.kaiserpfalzedv.billing.notitia.api.customer.EmailAddressTO;
import de.kaiserpfalzedv.billing.notitia.jpa.customer.JPACustomer;
import de.kaiserpfalzedv.billing.notitia.jpa.customer.JPACustomerUpdateContactEmailEvent;
import de.kaiserpfalzedv.billing.notitia.jpa.customer.JPAEmailAddress;
import de.kaiserpfalzedv.billing.notitia.services.customer.CustomerUpdateContactEmailExecutor;
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
public class CustomerUpdateContactEmailExecutorTest {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerUpdateContactEmailExecutorTest.class);

    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private static final String CUSTOMER_NAME = "Customer";
    private static final String COST_CENTER = "12345";
    private static final String CUSTOMER_MAIL_NAME = "Customer Info";
    private static final String CUSTOMER_MAIL_ADDRESS = "info@kaiserpfalz-edv.de";
    private static final UUID CUSTOMER_MAIL_ID = UUID.randomUUID();

    private static final EmailAddressTO EMAIL_ADDRESS = new EmailAddressTO(
            CUSTOMER_MAIL_ID, CUSTOMER_MAIL_NAME, CUSTOMER_MAIL_ADDRESS, "GENERIC"
    );
    private static final JPAEmailAddress JPA_EMAIL_ADDRESS = new JPAEmailAddress(EMAIL_ADDRESS);
    private static final HashMap<String, String> TAGS = new HashMap<>();
    private static final JPACustomer CUSTOMER = new JPACustomer();
    static {

        CUSTOMER.setId(CUSTOMER_ID);
        CUSTOMER.setName(CUSTOMER_NAME);
        CUSTOMER.setCostCenter(COST_CENTER);
        CUSTOMER.setBillingAddress(JPA_EMAIL_ADDRESS);
        CUSTOMER.setContactAddress(JPA_EMAIL_ADDRESS);

        TAGS.put("customer", "12345");
        CUSTOMER.setTags(TAGS);
    }


    private CustomerUpdateContactEmailExecutor service;

    @Mock
    private EntityManager em;
    @Mock
    private EntityTransaction tx;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Test
    public void shouldUpdateEmailWhenEventListenerIsCalled() throws CommandFailedException {
        logMethod("simple-call", "Updating email: {}", EMAIL_ADDRESS);

        CustomerUpdateContactEmailCommand command = new CustomerUpdateContactEmailCommand(CUSTOMER_ID, EMAIL_ADDRESS);

        when(em.find(JPACustomer.class, CUSTOMER_ID)).thenReturn(CUSTOMER);

        service.execute(command);

        verify(em, atLeastOnce()).merge(any(JPACustomer.class));
        verify(em, atLeastOnce()).persist(any(JPACustomerUpdateContactEmailEvent.class));
    }

    @Test(expected = CommandFailedException.class)
    public void shouldThrowCommandExceptionWhenPersistenceExceptionIsThrownOutsideTransaction() throws CommandFailedException {
        logMethod("persistence-failure-outside-transaction", "Updating email for failing outside of transaction: {}", EMAIL_ADDRESS);

        CustomerUpdateContactEmailCommand command = new CustomerUpdateContactEmailCommand(CUSTOMER_ID, EMAIL_ADDRESS);


        when(em.find(JPACustomer.class, CUSTOMER_ID)).thenReturn(CUSTOMER);
        doThrow(new PersistenceException()).when(em).persist(any(JPACustomerUpdateContactEmailEvent.class));
        when(em.isJoinedToTransaction()).thenReturn(false);
        
        service.execute(command);
    }


    @Test(expected = CommandFailedException.class)
    public void shouldThrowCommandExceptionWhenPersistenceExceptionIsThrownInsideTransaction() throws CommandFailedException {
        logMethod("persistence-failure-inside-transaction", "Updating email for failing in transaction: {}", EMAIL_ADDRESS);

        CustomerUpdateContactEmailCommand command = new CustomerUpdateContactEmailCommand(CUSTOMER_ID, EMAIL_ADDRESS);

        when(em.find(JPACustomer.class, CUSTOMER_ID)).thenReturn(CUSTOMER);
        doThrow(new PersistenceException()).when(em).persist(any(JPACustomerUpdateContactEmailEvent.class));
        when(em.isJoinedToTransaction()).thenReturn(true);
        when(tx.isActive()).thenReturn(true);

        service.execute(command);

        verify(tx, atLeastOnce()).setRollbackOnly();
    }

    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @Before
    public void setUp() {
        service = new CustomerUpdateContactEmailExecutor();
        service.setEntityManager(em);

        when(em.getTransaction()).thenReturn(tx);
    }

    @BeforeClass
    public static void setUpMDC() {
        MDC.put("test", "Update Customer Contact Email Executor");

        LOG.info("===[{}]========[BEGIN]===", MDC.get("test"));
    }

    @AfterClass
    public static void tearDownMDC() {
        LOG.info("===[{}]==========[END]===", MDC.get("test"));
        MDC.remove("id");
        MDC.remove("test");
    }
}
