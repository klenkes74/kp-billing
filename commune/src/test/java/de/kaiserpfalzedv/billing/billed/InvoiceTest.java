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

package de.kaiserpfalzedv.billing.billed;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.UUID;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import de.kaiserpfalzedv.billing.billed.impl.InvoiceBuilder;
import de.kaiserpfalzedv.billing.guided.Customer;
import de.kaiserpfalzedv.billing.guided.impl.CustomerBuilder;
import org.javamoney.moneta.Money;
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
 * @since 2018-02-12
 */
public class InvoiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceTest.class);

    private static final ZoneId UTC = ZoneId.of("UTC");
    private static final CurrencyUnit CURRENCY = Monetary.getCurrency("EUR");
    private static final MonetaryAmount ZERO_AMOUNT = Money.of(0L, CURRENCY);

    private static final UUID ID = UUID.randomUUID();
    private static final String INVOICE_NUMBER = "1";
    private static final LocalDate INVOICE_DATE = LocalDate.now(UTC).minusDays(2);
    private static final Customer CUSTOMER = new CustomerBuilder()
            .setName("TestCustomer")
            .setCostReference("101010")
            .build();
    private static final MonetaryAmount AMOUNT = ZERO_AMOUNT;

    private InvoiceBuilder service;

    @BeforeClass
    public static void setUpMDC() {
        MDC.put("test", Invoice.class.getSimpleName());

        if (!SLF4JBridgeHandler.isInstalled()) {
            SLF4JBridgeHandler.install();
        }
    }

    @AfterClass
    public static void tearDownMDC() {
        MDC.remove("id");
        MDC.remove("test");

        if (SLF4JBridgeHandler.isInstalled()) {
            SLF4JBridgeHandler.uninstall();
        }
    }

    @Test
    public void shouldCreateEmptyInvoiceItemWhenCalledWithMinimumParameters() {
        logMethod(
                "mimimum-invoice",
                "Should create an empty invoice when only minimum parameters are given"
        );

        Invoice result = service
                .setCustomer(CUSTOMER)
                .setCurrency(CURRENCY)
                .build();
        LOG.debug("Result: {}", result);

        assertNotNull("Id should have been generated!", result.getId());
        assertEquals("The invoice number should match the id!", result.getId().toString(), result.getInvoiceNumber());
        assertEquals("The invoice date should be today!", LocalDate.now(UTC), result.getInvoiceDate());
        assertEquals("The customer does not match!", CUSTOMER, result.getCustomer());
        assertEquals("The invoice should contain no parts!", 0, result.getParts().size());
        assertEquals("The invoice should contain no attachments!", 0, result.getAttachments().size());
        assertEquals("The amount does not match!", AMOUNT, result.getAmount());
    }

    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @Test
    public void shouldCreateNiceInvoicePartWhenCalledWithAllParameters() {
        logMethod(
                "full-invoice",
                "Should create a nice invoice when maximum parameters are given"
        );

        Invoice result = service
                .setCurrency(CURRENCY)
                .setId(ID)
                .setInvoiceNumber(INVOICE_NUMBER)
                .setInvoiceDate(INVOICE_DATE)
                .addPart("Test", new ArrayList<>(0))
                .addPart("Test 2", new ArrayList<>(0), Monetary.getCurrency("USD"))
                .setCustomer(CUSTOMER)
                .setAmount(AMOUNT)
                .build();
        LOG.debug("Result: {}", result);

        assertEquals("The ID does not match!", ID, result.getId());
        assertEquals("The invoice number does match!", INVOICE_NUMBER, result.getInvoiceNumber());
        assertEquals("The invoice date does not match!", INVOICE_DATE, result.getInvoiceDate());
        assertEquals("The customer does not match!", CUSTOMER, result.getCustomer());
        assertEquals("The number of parts does not match!", 2, result.getParts().size());
        assertEquals("The amount does not match!", AMOUNT, result.getAmount());
    }

    @Test(timeout = 1000L)
    public void shouldNotExceedTimeLimitOf1000msWhenCalled1000Times() {
        logMethod("runtime-validation",
                  "Should not exceed the timelimit of {} ms for {} iterations",
                  1000L, 1000L
        );

        for (int i = 0; i < 1000; i++) {
            service
                    .setCustomer(CUSTOMER)
                    .setCurrency(CURRENCY)
                    .build();
        }
    }

    @Test(expected = IllegalStateException.class)
    public void shouldFialWhenCalledWithoutCustomer() {
        logMethod("fail-without-customer", "Should fail when no customer is given");

        service
                .setCurrency(CURRENCY)
                .build();
    }

    @Before
    public void setUp() {
        service = new InvoiceBuilder();
    }
}
