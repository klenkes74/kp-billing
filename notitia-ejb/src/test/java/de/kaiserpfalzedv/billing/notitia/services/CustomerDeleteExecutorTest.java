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

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import de.kaiserpfalzedv.billing.notitia.api.commands.CommandFailedException;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerDeleteCommand;
import de.kaiserpfalzedv.billing.notitia.jpa.customer.JPACustomer;
import de.kaiserpfalzedv.billing.notitia.jpa.customer.JPACustomerCreateEvent;
import de.kaiserpfalzedv.billing.notitia.services.customer.CustomerDeleteExecutor;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-25
 */
public class CustomerDeleteExecutorTest {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerDeleteExecutorTest.class);

    private static final UUID CUSTOMER_ID = UUID.randomUUID();


    private CustomerDeleteExecutor service;

    @Mock
    private EntityManager em;
    @Mock
    private EntityTransaction tx;
    @Mock
    private JPACustomer customer;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Test
    public void shouldDeleteCustomerWhenEventListenerIsCalled() throws CommandFailedException {
        logMethod("simple-call", "Deleting customer: {}", CUSTOMER_ID);

        CustomerDeleteCommand command = new CustomerDeleteCommand(CUSTOMER_ID);

        when(em.find(JPACustomer.class, CUSTOMER_ID)).thenReturn(customer);

        service.execute(command);
    }


    @Test
    public void shouldReportCustomerWhenCustomerDidNotExist() throws CommandFailedException {
        logMethod("simple-call-not-existing-customer", "Deleting customer: {}", CUSTOMER_ID);

        CustomerDeleteCommand command = new CustomerDeleteCommand(CUSTOMER_ID);

        service.execute(command);
    }


    @Test(expected = CommandFailedException.class)
    public void shouldThrowCommandExceptionWhenPersistenceExceptionIsThrown() throws CommandFailedException {
        logMethod("persistence-failure-outside-transaction", "Creating customer: {}", CUSTOMER_ID);

        CustomerDeleteCommand command = new CustomerDeleteCommand(CUSTOMER_ID);

        when(em.find(eq(JPACustomer.class), any())).thenThrow(new PersistenceException());

        service.execute(command);
    }

    @Test(expected = CommandFailedException.class)
    public void shouldThrowCommandExceptionWhenPersistenceExceptionIsThrownInsideTransaction() throws CommandFailedException {
        logMethod("persistence-failure-inside-transaction", "Creating customer: {}", CUSTOMER_ID);

        CustomerDeleteCommand command = new CustomerDeleteCommand(CUSTOMER_ID);

        when(em.find(eq(JPACustomer.class), any())).thenThrow(new PersistenceException());

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
        service = new CustomerDeleteExecutor();
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
