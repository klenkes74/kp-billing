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

package de.kaiserpfalzedv.billing.common;

import java.util.UUID;

import javax.xml.registry.JAXRException;

import de.kaiserpfalzedv.billing.common.impl.EmailAddressBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class EmailAddressTest {
    private static final Logger LOG = LoggerFactory.getLogger(EmailAddressTest.class);

    private static final UUID ID = UUID.randomUUID();
    private static final String NAME = "unit test";
    private static final String ADDRESS = "unittest@kaiserpfalz-edv.de";
    private static final String TYPE = "unittest";
    private static final EmailAddress EMAIL_ADDRESS = new EmailAddressBuilder()
            .setId(ID)
            .setName(NAME)
            .setAddress(ADDRESS)
            .setType(TYPE)
            .build();

    private EmailAddressBuilder service;

    @BeforeClass
    public static void setUpClass() {
        MDC.put("test", "EmailAddressTest");
    }

    @AfterClass
    public static void tearDownClass() {
        MDC.remove("test");
        MDC.remove("id");
    }

    @Test
    public void generateSimpleEmail() throws JAXRException {
        logMethod("simple-email", "Testing a simple email: {}", ADDRESS);

        EmailAddress result = service.setAddress(ADDRESS).build();
        LOG.debug("result: {}", result);

        assertNotNull("The id should default to a random UUID", result.getId());
        assertEquals("Name should be empty", "", result.getName());
        assertEquals("Wrong email address", ADDRESS, result.getAddress());
        assertEquals("Wrong email type", EmailAddressBuilder.CONTACT_EMAIL, result.getType());
    }

    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @Test
    public void copyEmail() throws JAXRException {
        logMethod("copy-email", "Copying the email address: {}", EMAIL_ADDRESS);

        EmailAddress result = service.copy(EMAIL_ADDRESS).build();
        LOG.debug("result: {}", result);

        assertEquals("ID does not match", ID, result.getId());
        assertEquals("Name does not match", NAME, result.getName());
        assertEquals("Email address does not match", ADDRESS, result.getAddress());
        assertEquals("Type does not match", TYPE, result.getType());
    }

    @Test(timeout = 100L)
    public void runtimeTest() {
        logMethod("runtime-verification", "checking the runtime of the email address builder ...");

        for (int i = 0; i < 1000; i++) {
            service.setAddress(ADDRESS).build();
        }
    }

    @Before
    public void setUpService() {
        service = new EmailAddressBuilder();
    }

    @After
    public void tearDownService() {
        MDC.remove("id");
    }
}
