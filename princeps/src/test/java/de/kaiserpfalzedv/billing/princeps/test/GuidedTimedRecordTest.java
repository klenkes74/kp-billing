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

package de.kaiserpfalzedv.billing.princeps.test;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import de.kaiserpfalzedv.billing.api.guided.Customer;
import de.kaiserpfalzedv.billing.api.guided.GuidedTimedRecord;
import de.kaiserpfalzedv.billing.api.guided.ProductInfo;
import de.kaiserpfalzedv.billing.api.guided.ProductRecordInfo;
import de.kaiserpfalzedv.billing.princeps.CustomerBuilder;
import de.kaiserpfalzedv.billing.princeps.GuidedRecordBuilder;
import de.kaiserpfalzedv.billing.princeps.ProductInfoBuilder;
import de.kaiserpfalzedv.billing.princeps.ProductRecordInfoBuilder;
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
import static org.junit.Assert.assertNotNull;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-11
 */
public class GuidedTimedRecordTest {
    private static final Logger LOG = LoggerFactory.getLogger(GuidedTimedRecordTest.class);

    private static final ZoneId UTC = ZoneId.of("UTC");

    private static final UUID ID = UUID.randomUUID();
    private static final String METERING_ID = "guided-id";


    private static final OffsetDateTime RECORDED_DATE = OffsetDateTime.now(UTC);
    private static final OffsetDateTime IMPORT_DATE = OffsetDateTime.now(UTC);
    private static final OffsetDateTime VALUE_DATE = OffsetDateTime.now(UTC);

    private static final Duration METERED_DURATION = Duration.ofHours(24L);
    private static final OffsetDateTime METERED_START_DATE = VALUE_DATE.minus(METERED_DURATION);

    private static final Customer CUSTOMER = new CustomerBuilder()
            .setName("customer")
            .setCostReference("customer-costcenter")
            .build();

    private static final ArrayList<String> TAG_NAMES = new ArrayList<>(4);
    static {
        TAG_NAMES.add("cluster");
        TAG_NAMES.add("project");
        TAG_NAMES.add("pod");
        TAG_NAMES.add("customer");
    }

    private static final ProductInfo PRODUCT_INFO = new ProductInfoBuilder()
            .setName("Cluster CPU Usage")
            .setTags(TAG_NAMES)
            .build();


    private static final HashMap<String, String> TAGS = new HashMap<>(4);
    static {
        TAGS.put("cluster", "abbot1");
        TAGS.put("project", "billing");
        TAGS.put("pod", "princeps-8fdg2");
        TAGS.put("customer", "982341");
    }

    private static final ProductRecordInfo PRODUCT_RECORD_INFO = new ProductRecordInfoBuilder()
            .setProductInfo(PRODUCT_INFO)
            .setTags(TAGS)
            .build();

    private static final GuidedTimedRecord GUIDED_METERED_RECORD = new GuidedRecordBuilder<GuidedTimedRecord>()
            .setId(ID)
            .setMeteringId(METERING_ID)
            .setCustomer(CUSTOMER)
            .setRecordedDate(RECORDED_DATE)
            .setImportedDate(IMPORT_DATE)
            .setValueDate(VALUE_DATE)
            .setMeteredTimestamp(METERED_START_DATE)
            .setMeteredDuration(METERED_DURATION)
            .setProductInfo(PRODUCT_RECORD_INFO)
            .build();

    private GuidedRecordBuilder<GuidedTimedRecord> service;

    @BeforeClass
    public static void setUpClass() {
        MDC.put("test", GuidedTimedRecord.class.getSimpleName());

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
    public void generateSimpleGuidedTimedRecord() {
        logMethod("guided-timed-record", "Testing a simple guided timed record");

        GuidedTimedRecord result = service
                .setProductInfo(PRODUCT_RECORD_INFO)
                .setCustomer(CUSTOMER)
                .setMeteredTimestamp(METERED_START_DATE)
                .setMeteredDuration(METERED_DURATION)
                .build();
        LOG.debug("result: {}", result);

        assertNotNull("The id should default to a random UUID", result.getId());
        assertEquals("The metering-id does not match the id", result.getId().toString(), result.getMeteringId());
        assertEquals("The metered customer does not match", CUSTOMER, result.getCustomer());
        assertEquals("The product info does not match", PRODUCT_RECORD_INFO, result.getProductInfo());
        assertEquals("The metered start date does not match", METERED_START_DATE, result.getMeteredTimestamp());
        assertEquals("The metered duration does not match", METERED_DURATION, result.getMeteredDuration());

        assertNotNull("The value date does not exist", result.getValueDate());
        assertEquals("The value date and recorded date does not match", result.getValueDate(), result.getRecordedDate());
        assertEquals("The value date and imported date does not match", result.getValueDate(), result.getImportedDate());
    }

    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @Test
    public void copyGuidedTimedRecord() {
        logMethod("copy-guided-timed-record", "Copying the guided timed record: {}", GUIDED_METERED_RECORD);

        GuidedTimedRecord result = service.copy(GUIDED_METERED_RECORD).build();
        LOG.debug("result: {}", result);

        assertEquals("ID does not match", ID, result.getId());
        assertEquals("The metering-id does not match", METERING_ID, result.getMeteringId());
        assertEquals("The metered customer does not match", CUSTOMER, result.getCustomer());
        assertEquals("The product info does not match", PRODUCT_RECORD_INFO, result.getProductInfo());
        assertEquals("The metered start date does not match", METERED_START_DATE, result.getMeteredTimestamp());
        assertEquals("The metered duration does not match", METERED_DURATION, result.getMeteredDuration());

        assertEquals("The value date does not match", VALUE_DATE, result.getValueDate());
        assertEquals("The recorded date does not match", RECORDED_DATE, result.getRecordedDate());
        assertEquals("The imported date does not match", IMPORT_DATE, result.getImportedDate());
    }

    @Test(timeout = 80L)
    public void runtimeTest() {
        logMethod("runtime-verification", "checking the runtime of the tarif builder ...");

        for (int i = 0; i < 1000; i++) {
            service
                    .setProductInfo(PRODUCT_RECORD_INFO)
                    .setCustomer(CUSTOMER)
                    .setMeteredTimestamp(METERED_START_DATE)
                    .setMeteredDuration(METERED_DURATION)
                    .build();
        }
    }

    @Before
    public void setUp() throws Exception {
        service = new GuidedRecordBuilder<>();
    }

    @After
    public void tearDown() throws Exception {
        MDC.remove("id");
    }

}