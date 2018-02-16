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

package de.kaiserpfalzedv.billing.ratio.test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import de.kaiserpfalzedv.billing.api.guided.Customer;
import de.kaiserpfalzedv.billing.api.guided.ProductInfo;
import de.kaiserpfalzedv.billing.api.guided.ProductRecordInfo;
import de.kaiserpfalzedv.billing.api.rated.RatedMeteredRecord;
import de.kaiserpfalzedv.billing.api.rated.Tarif;
import de.kaiserpfalzedv.billing.princeps.CustomerBuilder;
import de.kaiserpfalzedv.billing.princeps.ProductInfoBuilder;
import de.kaiserpfalzedv.billing.princeps.ProductRecordInfoBuilder;
import de.kaiserpfalzedv.billing.ratio.RatedRecordBuilder;
import de.kaiserpfalzedv.billing.ratio.TarifBuilder;
import org.javamoney.moneta.internal.MoneyAmountBuilder;
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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-11
 */
public class TarifedMeteredRecordTest {
    private static final Logger LOG = LoggerFactory.getLogger(TarifedMeteredRecordTest.class);

    private static final ZoneId UTC = ZoneId.of("UTC");
    private static final CurrencyUnit EUR = Monetary.getCurrency("EUR");


    private static final UUID ID = UUID.randomUUID();
    private static final String METERING_ID = "metered-id";

    private static final BigDecimal METERED_VALUE = BigDecimal.valueOf(25L);

    private static final OffsetDateTime RECORDED_DATE = OffsetDateTime.now(UTC);
    private static final OffsetDateTime IMPORT_DATE = OffsetDateTime.now(UTC);
    private static final OffsetDateTime VALUE_DATE = OffsetDateTime.now(UTC);

    private static final Customer CUSTOMER = new CustomerBuilder()
            .setName("customer")
            .setCostReference("customer-costcenter")
            .build();

    private static final ProductInfo PRODUCT_INFO = new ProductInfoBuilder()
            .setName("Cluster CPU Usage")
            .setTags(new String[]{"cluster", "project"})
            .build();

    private static final ProductRecordInfo PRODUCT_RECORD_INFO = new ProductRecordInfoBuilder()
            .setProductInfo(PRODUCT_INFO)
            .setTags(new String[]{"abbot1", "billing"})
            .build();

    private static final Tarif TARIF = new TarifBuilder()
            .setTarifName("CPU usage")
            .setUnit("EUR/mCores h")
            .setRate(
                    new MoneyAmountBuilder()
                            .setNumber(BigDecimal.TEN)
                            .setCurrency(EUR)
                            .create()
            )
            .setUnitDivisor(BigDecimal.valueOf(1000L))
            .build();

    private static final MonetaryAmount AMOUNT = new MoneyAmountBuilder()
            .setNumber(0.25d)
            .setCurrency(EUR)
            .create();


    private static final RatedMeteredRecord TARIFED_METERED_RECORD = new RatedRecordBuilder<RatedMeteredRecord>()
            .setId(ID)
            .setMeteringId(METERING_ID)
            .setRecordedDate(RECORDED_DATE)
            .setImportedDate(IMPORT_DATE)
            .setValueDate(VALUE_DATE)
            .setProductInfo(PRODUCT_RECORD_INFO)
            .setCustomer(CUSTOMER)
            .setMeteredValue(METERED_VALUE)
            .setTarif(TARIF)
            .build();

    private RatedRecordBuilder<RatedMeteredRecord> service;

    @BeforeClass
    public static void setUpClass() {
        MDC.put("test", RatedMeteredRecord.class.getSimpleName());

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
    public void generateSimpleTarifedMeteredRecord() {
        logMethod("metered-tarifed-record", "Testing a simple tarifed metered record");

        RatedMeteredRecord result = service
                .setProductInfo(PRODUCT_RECORD_INFO)
                .setCustomer(CUSTOMER)
                .setMeteredValue(METERED_VALUE)
                .setTarif(TARIF)
                .build();
        LOG.debug("result: {}", result);

        assertNotNull("The id should default to a random UUID", result.getId());
        assertEquals("The metering-id does not match the id", result.getId().toString(), result.getMeteringId());
        assertEquals("The metered customer does not match", CUSTOMER, result.getCustomer());
        assertEquals("The product record info does not match", PRODUCT_RECORD_INFO, result.getProductInfo());
        assertEquals("The tarif does not match", TARIF, result.getTarif());
        assertEquals("The amount does not match", AMOUNT, result.getAmount());
        assertEquals("The metered value does not match", METERED_VALUE, result.getMeteredValue());

        assertNotNull("The value date does not exist", result.getValueDate());
        assertEquals("The value date and recorded date does not match", result.getValueDate(), result.getRecordedDate());
        assertEquals("The value date and imported date does not match", result.getValueDate(), result.getImportedDate());
    }

    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @Test
    public void copyTarifedMeteredRecord() {
        logMethod("copy-tarifed-metered-record", "Copying the tarifed metered record: {}", TARIFED_METERED_RECORD);

        RatedMeteredRecord result = service.copy(TARIFED_METERED_RECORD).build();
        LOG.debug("result: {}", result);

        assertEquals("ID does not match", ID, result.getId());
        assertNotEquals("System.identityHashCode does not differ", System.identityHashCode(TARIFED_METERED_RECORD), System
                .identityHashCode(result));
        assertEquals("The metering-id does not match", METERING_ID, result.getMeteringId());
        assertEquals("The metered customer does not match", CUSTOMER, result.getCustomer());
        assertEquals("The product record info does not match", PRODUCT_RECORD_INFO, result.getProductInfo());
        assertEquals("The metered value does not match", METERED_VALUE, result.getMeteredValue());

        assertEquals("The value date does not match", VALUE_DATE, result.getValueDate());
        assertEquals("The recorded date does not match", RECORDED_DATE, result.getRecordedDate());
        assertEquals("The imported date does not match", IMPORT_DATE, result.getImportedDate());
    }

    @Test(timeout = 900L)
    public void runtimeTest() {
        logMethod("runtime-verification", "checking the runtime of the tarif builder ...");

        for (int i = 0; i < 1000; i++) {
            service
                    .setProductInfo(PRODUCT_RECORD_INFO)
                    .setCustomer(CUSTOMER)
                    .setMeteredValue(METERED_VALUE)
                    .setTarif(TARIF)
                    .build();
        }
    }

    @Before
    public void setUp() throws Exception {
        service = new RatedRecordBuilder<>();
    }

    @After
    public void tearDown() throws Exception {
        MDC.remove("id");
    }
}