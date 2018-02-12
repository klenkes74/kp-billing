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

import java.util.ArrayList;
import java.util.UUID;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import de.kaiserpfalzedv.billing.billed.impl.InvoiceItemBuilder;
import de.kaiserpfalzedv.billing.billed.impl.InvoicePartBuilder;
import org.javamoney.moneta.Money;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.bridge.SLF4JBridgeHandler;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-12
 */
public class InvoicePartTest {
    private static final Logger LOG = LoggerFactory.getLogger(InvoicePartTest.class);

    private static final CurrencyUnit CURRENCY = Monetary.getCurrency("EUR");

    private static final UUID ITEM_ID_1 = UUID.randomUUID();
    private static final String ITEM_ITEM_ID_1 = "1";
    private static final String ITEM_TITLE_1 = "Item 1";
    private static final MonetaryAmount ITEM_AMOUNT_1 = Money.of(1000L, CURRENCY);
    private static final InvoiceItem ITEM_1 = new InvoiceItemBuilder()
            .setId(ITEM_ID_1)
            .setItemId(ITEM_ITEM_ID_1)
            .setTitle(ITEM_TITLE_1)
            .setAmount(ITEM_AMOUNT_1)
            .build();

    private static final UUID ITEM_ID_2 = UUID.randomUUID();
    private static final String ITEM_ITEM_ID_2 = "2";
    private static final String ITEM_TITLE_2 = "Item 2";
    private static final MonetaryAmount ITEM_AMOUNT_2 = Money.of(50L, CURRENCY);
    private static final InvoiceItem ITEM_2 = new InvoiceItemBuilder()
            .setId(ITEM_ID_2)
            .setItemId(ITEM_ITEM_ID_2)
            .setTitle(ITEM_TITLE_2)
            .setAmount(ITEM_AMOUNT_2)
            .build();

    private static final ArrayList<InvoiceItem> INVOICE_ITEMS = new ArrayList<>(2);
    private static final UUID ID = UUID.randomUUID();
    private static final String TITLE = "Invoice Part Text";
    private static final MonetaryAmount AMOUNT = ITEM_AMOUNT_1.add(ITEM_AMOUNT_2);
    private static final MonetaryAmount ZERO_AMOUNT = Money.of(0L, CURRENCY);

    static {
        INVOICE_ITEMS.add(ITEM_1);
        INVOICE_ITEMS.add(ITEM_2);
    }

    private InvoicePartBuilder service;

    @BeforeClass
    public static void setUpMDC() {
        MDC.put("test", InvoicePart.class.getSimpleName());

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
                "mimimum-invoice-part",
                "Should create an empty invoice part when only minimum parameters are given"
        );

        InvoicePart result = service
                .setTitle(TITLE)
                .setAmount(AMOUNT)
                .setCurrency(CURRENCY)
                .build();
        LOG.debug("Result: {}", result);

        assertNotNull("Id should have been generated!", result.getId());
        assertEquals("The title does not match!", TITLE, result.getTitle());
        assertEquals("The amount does not match!", AMOUNT, result.getAmount());
    }

    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @Test
    public void shouldCreateNiceInvoicePartWhenCalledWithAllParameters() {
        logMethod(
                "full-invoice-part",
                "Should create a nice invoice part when maximum parameters are given"
        );

        InvoicePart result = service
                .setId(ID)
                .setTitle(TITLE)
                .setItems(INVOICE_ITEMS)
                .setCurrency(CURRENCY)
                .build();
        LOG.debug("Result: {}", result);

        assertEquals("The ID does not match!", ID, result.getId());
        assertEquals("The title does not match!", TITLE, result.getTitle());
        assertArrayEquals("The items do not match!", INVOICE_ITEMS.toArray(new InvoiceItem[0]), result.getItems());
        assertEquals("The amount does not match!", AMOUNT, result.getAmount());
    }

    @Test
    public void shouldCreateNiceInvoicePartWhenCalledWithAllParametersAndAllItemsAreReplaced() {
        logMethod(
                "invoice-part-item-replacement",
                "Should create a nice invoice part when items are replaced"
        );

        InvoicePart result = service
                .setId(ID)
                .setTitle(TITLE)
                .setItems(INVOICE_ITEMS)
                .setCurrency(CURRENCY)
                .clearItems()
                .addItem(ITEM_TITLE_2, ITEM_AMOUNT_2)
                .addItem(ITEM_ID_1, ITEM_ITEM_ID_1, ITEM_TITLE_1, ITEM_AMOUNT_1)
                .build();
        LOG.debug("Result: {}", result);

        ArrayList<InvoiceItem> items = new ArrayList<>(2);
        items.add(new InvoiceItemBuilder().setTitle(ITEM_TITLE_2).setAmount(ITEM_AMOUNT_2).build());
        items.add(
                new InvoiceItemBuilder()
                        .setId(ITEM_ID_1)
                        .setItemId(ITEM_ITEM_ID_1)
                        .setTitle(ITEM_TITLE_1)
                        .setAmount(ITEM_AMOUNT_1)
                        .build()
        );

        assertEquals("The ID does not match!", ID, result.getId());
        assertEquals("The title does not match!", TITLE, result.getTitle());
        assertEquals("The item 1 title does not match!", items.toArray(new InvoiceItem[0])[0].getTitle(), result.getItems()[0]
                .getTitle());
        assertEquals("The item 1 amount does not match!", items.toArray(new InvoiceItem[0])[0].getAmount(), result.getItems()[0]
                .getAmount());
        assertEquals("The item 2 does not match!", items.toArray(new InvoiceItem[0])[1], result.getItems()[1]);
        assertEquals("The amount does not match!", AMOUNT, result.getAmount());
    }

    @Test
    public void shouldCreateZeroValuedInvoiceItemWhenCalledWithoutItemsParameters() {
        logMethod(
                "zero-amount-invoice-part",
                "Should create a zero amount invoice part when no amount is given"
        );

        InvoicePart result = service
                .setId(ID)
                .setTitle(TITLE)
                .setCurrency(CURRENCY)
                .build();
        LOG.debug("Result: {}", result);

        assertEquals("The ID does not match!", ID, result.getId());
        assertEquals("The title does not match!", TITLE, result.getTitle());
        assertEquals("The amount does not match!", ZERO_AMOUNT, result.getAmount());
    }

    @Test(timeout = 1000L)
    public void shouldNotExceedTimeLimitOf1000msWhenCalled1000Times() {
        logMethod("runtime-validation",
                  "Should not exceed the timelimit of {} ms for {} iterations",
                  1000L, 1000L
        );

        for (int i = 0; i < 1000; i++) {
            service
                    .setTitle(TITLE)
                    .setAmount(AMOUNT)
                    .setCurrency(CURRENCY)
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
        service = new InvoicePartBuilder();
    }
}
