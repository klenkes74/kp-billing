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

package de.kaiserpfalzedv.billing.notitia.jpa.test;

import java.util.HashMap;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import de.kaiserpfalzedv.billing.notitia.jpa.customer.JPACustomer;
import de.kaiserpfalzedv.billing.notitia.jpa.customer.JPAEmailAddress;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static org.junit.Assert.assertEquals;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-20
 */
public class JPACustomerIT {
    private static final Logger LOG = LoggerFactory.getLogger(JPACustomerIT.class);

    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private static final UUID BILLLING_EMAIL_ID = UUID.randomUUID();
    private static final UUID CONTACT_EMAIL_ID = UUID.randomUUID();

    private static EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    @Test
    public void shouldCreateADatabaseWhenDoingNothing() {
        logMethod("save-customer",
                  "Simply save customer with id '{}' (billing email id '{}', contact email id '{}')",
                  CUSTOMER_ID, BILLLING_EMAIL_ID, CONTACT_EMAIL_ID);

        JPAEmailAddress billingEmail = new JPAEmailAddress();
        billingEmail.setId(BILLLING_EMAIL_ID);
        billingEmail.setAddress("rlichti@kaiserpfalz-edv.de");
        billingEmail.setName("Roland T. Lichti");
        billingEmail.setType("BILLING");

        JPAEmailAddress contactEmail = new JPAEmailAddress();
        contactEmail.setId(CONTACT_EMAIL_ID);
        contactEmail.setAddress("rlichti@kaiserpfalz-edv.de");
        contactEmail.setName("Roland T. Lichti");
        contactEmail.setType("CONTACT");

        HashMap<String, String> customerTags = new HashMap<>();
        customerTags.put("test", "testdata");
        customerTags.put("test2", "testdata2");

        JPACustomer customer = new JPACustomer();
        customer.setId(CUSTOMER_ID);
        customer.setName("TestCustomer");
        customer.setCostReference("23141");
        customer.setBillingAddress(billingEmail);
        customer.setContactAddress(contactEmail);
        customer.setTags(customerTags);

        em.persist(customer);
    }


    @Test
    public void shouldHaveCustomerRolandLichtiWhenSelectedByCostCenter92131() {
        JPACustomer result = em
                .createQuery("select c from Customers c where name = :name", JPACustomer.class)
                .setParameter("name", "Roland Lichti")
                .getSingleResult();
        LOG.trace("Result: {}", result);

        assertEquals("Wrong UUID", UUID.fromString("4136ac16-6520-4234-ae86-db81912f0dbc"), result.getId());
    }



    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @Before
    public void setUp() {
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
    }

    @After
    public void tearDown() {
        tx.commit();
        em.close();
    }

    @BeforeClass
    public static void setUpMDC() {
        MDC.put("test", JPACustomer.class.getSimpleName());

        LOG.info("===[{}]========[BEGIN]===", MDC.get("test"));
    }

    @BeforeClass
    public static void setUpEntityManagerFactory() {
        emf = Persistence.createEntityManagerFactory("notitia-test");
    }

    @AfterClass
    public static void tearDownMDC() {
        LOG.info("===[{}]==========[END]===", MDC.get("test"));
        MDC.remove("id");
        MDC.remove("test");
    }

    @AfterClass
    public static void tearDownEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

}
