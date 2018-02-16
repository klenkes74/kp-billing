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

package de.kaiserpfalzedv.billing.libellum;

import java.util.UUID;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import de.kaiserpfalzedv.billing.api.billed.InvoiceItem;
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
public class InvoiceItemTest {
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceItemTest.class);


    private static final UUID ID = UUID.randomUUID();
    private static final String ITEM_ID = "99";
    private static final String TITLE = "Invoice Item Text";
    private static final CurrencyUnit CURRENCY = Monetary.getCurrency("EUR");
    private static final MonetaryAmount AMOUNT = Money.of(10, CURRENCY);
    private static final MonetaryAmount ZERO_AMOUNT = Money.of(0, CURRENCY);


    private InvoiceItemBuilder service;

    @BeforeClass
    public static void setUpMDC() {
        MDC.put("test", InvoiceItem.class.getSimpleName());

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
                "mimimum-invoice-item",
                "Should create an empty invoice item when only minimum parameters are given"
        );

        InvoiceItem result = service
                .setTitle(TITLE)
                .setAmount(AMOUNT)
                .build();
        LOG.debug("Result: {}", result);

        assertNotNull("Id should have been generated!", result.getId());
        assertEquals("ItemId should have been generated from ID!", result.getId().toString(), result.getItemId());
        assertEquals("The title does not match!", TITLE, result.getTitle());
        assertEquals("The amount does not match!", AMOUNT, result.getAmount());
    }

    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @Test
    public void shouldCreateNiceInvoiceItemWhenCalledWithAllParameters() {
        logMethod(
                "full-invoice-item",
                "Should create a nice invoice item when maximum parameters are given"
        );

        InvoiceItem result = service
                .setId(ID)
                .setItemId(ITEM_ID)
                .setTitle(TITLE)
                .setAmount(AMOUNT)
                .build();
        LOG.debug("Result: {}", result);

        assertEquals("The ID does not match!", ID, result.getId());
        assertEquals("ItemId does not match!", ITEM_ID, result.getItemId());
        assertEquals("The title does not match!", TITLE, result.getTitle());
        assertEquals("The amount does not match!", AMOUNT, result.getAmount());
    }

    @Test
    public void shouldCreateZeroValuedInvoiceItemWhenCalledWithoutAmountParameters() {
        logMethod(
                "zero-amount-invoice-item",
                "Should create a zero amount invoice item when no amount is given"
        );

        InvoiceItem result = service
                .setId(ID)
                .setItemId(ITEM_ID)
                .setTitle(TITLE)
                .setCurrency(CURRENCY)
                .build();
        LOG.debug("Result: {}", result);

        assertEquals("The ID does not match!", ID, result.getId());
        assertEquals("ItemId does not match!", ITEM_ID, result.getItemId());
        assertEquals("The title does not match!", TITLE, result.getTitle());
        assertEquals("The amount does not match!", ZERO_AMOUNT, result.getAmount());
    }

    @Test(timeout = 500L)
    public void shouldNotExceedTimeLimitOf500msWhenCalled1000Times() {
        logMethod("runtime-validation",
                  "Should not exceed the timelimit of {} ms for {} iterations",
                  500L, 1000L
        );

        for (int i = 0; i < 1000; i++) {
            service
                    .setTitle(TITLE)
                    .setAmount(AMOUNT)
                    .build();
        }
    }

    @Test(expected = IllegalStateException.class)
    public void shouldFailWhenCalledWithoutAmountAndCurrencyParameters() {
        logMethod(
                "fail-without-amount-and-currency",
                "Should fail when neither amount nor currency is specified"
        );

        service
                .setTitle(TITLE)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldFialWhenCalledWithoutItemTitle() {
        logMethod("fail-without-title", "Should fail when no title is given");

        service
                .setCurrency(CURRENCY)
                .build();
    }

    @Before
    public void setUp() {
        service = new InvoiceItemBuilder();
    }
}
