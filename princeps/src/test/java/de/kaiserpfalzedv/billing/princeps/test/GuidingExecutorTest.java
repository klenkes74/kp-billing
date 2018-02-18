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

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import de.kaiserpfalzedv.billing.api.guided.Customer;
import de.kaiserpfalzedv.billing.api.guided.CustomerGuide;
import de.kaiserpfalzedv.billing.api.guided.GuidedMeteredRecord;
import de.kaiserpfalzedv.billing.api.guided.GuidedTimedRecord;
import de.kaiserpfalzedv.billing.api.guided.GuidingBusinessException;
import de.kaiserpfalzedv.billing.api.guided.GuidingExecutor;
import de.kaiserpfalzedv.billing.api.guided.ProductGuide;
import de.kaiserpfalzedv.billing.api.guided.ProductInfo;
import de.kaiserpfalzedv.billing.api.guided.ProductRecordInfo;
import de.kaiserpfalzedv.billing.api.imported.ImportingException;
import de.kaiserpfalzedv.billing.api.imported.RawBaseRecord;
import de.kaiserpfalzedv.billing.api.imported.RawMeteredRecord;
import de.kaiserpfalzedv.billing.api.imported.RawTimedRecord;
import de.kaiserpfalzedv.billing.invectio.RawBillingRecordBuilder;
import de.kaiserpfalzedv.billing.invectio.csv.CSVImporter;
import de.kaiserpfalzedv.billing.princeps.api.CustomerBuilder;
import de.kaiserpfalzedv.billing.princeps.GuidingExecutorImpl;
import de.kaiserpfalzedv.billing.princeps.api.ProductInfoBuilder;
import de.kaiserpfalzedv.billing.princeps.api.ProductRecordInfoBuilder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.bridge.SLF4JBridgeHandler;

import static java.time.ZoneOffset.UTC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-15
 */
public class GuidingExecutorTest {
    private static final Logger LOG = LoggerFactory.getLogger(GuidingExecutorTest.class);

    private static final HashMap<String, String> TAGS = new HashMap<>(4);
    static {
        TAGS.put("cluster", "abbot1");
        TAGS.put("project", "billing");
        TAGS.put("pod", "princeps-8fdg2");
        TAGS.put("customer", "982341");
    }
    
    private GuidingExecutor service;

    private ProductGuide productGuide = new TestProductGuide();
    private CustomerGuide customerGuide = new TestCustomerGuide();

    @Test
    public void shouldGenerateGuidedRecordsFromRawMeteredRecord() throws GuidingBusinessException {
        logMethod("simple-metered", "Create a guided record from a raw metered record");

        RawMeteredRecord record = new RawBillingRecordBuilder<RawMeteredRecord>()
                .setId(UUID.randomUUID())
                .setImportedDate(OffsetDateTime.now(UTC))
                .setMeteredDuration(Duration.ofMinutes(15L))
                .setMeteredValue(BigDecimal.TEN)
                .setTags(TAGS)
                .build();

        GuidedMeteredRecord result = record.execute(service, productGuide, customerGuide);
        LOG.trace("Result: {}", result);

        assertEquals("ID does not match!", record.getId(), result.getId());
        assertEquals("Metered value does not match!", record.getMeteredValue(), result.getMeteredValue());
        assertNotNull("No valid product added!", result.getProductInfo());
        assertNotNull("No valid customer added!", result.getCustomer());
    }

    @Test
    public void shouldGenerateGuidedRecordsFromRawTimedRecord() throws GuidingBusinessException {
        logMethod("simple-timped", "Create a guided record from a raw timed record");

        RawTimedRecord record = new RawBillingRecordBuilder<RawTimedRecord>()
                .setId(UUID.randomUUID())
                .setImportedDate(OffsetDateTime.now(UTC))
                .setMeteredDuration(Duration.ofMinutes(15L))
                .setTags(TAGS)
                .build();

        GuidedTimedRecord result = record.execute(service, productGuide, customerGuide);
        LOG.trace("Result: {}", result);

        assertEquals("ID does not match!", record.getId(), result.getId());
        assertEquals("Metered start time does not match!", record.getMeteredTimestamp(), result.getMeteredTimestamp());
        assertEquals("Duration does not match!", record.getMeteredDuration(), result.getMeteredDuration());
        assertNotNull("No valid product added!", result.getProductInfo());
        assertNotNull("No valid customer added!", result.getCustomer());
    }



    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @Before
    public void setUp() throws FileNotFoundException, ImportingException {
        service = new GuidingExecutorImpl();
    }

    @BeforeClass
    public static void setUpMDC() {
        MDC.put("test", CSVImporter.class.getSimpleName());

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

    private static final ArrayList<String> TAG_NAMES = new ArrayList<>(4);
    static {
        TAG_NAMES.add("cluster");
        TAG_NAMES.add("project");
        TAG_NAMES.add("pod");
        TAG_NAMES.add("customer");
    }

    private class TestProductGuide implements ProductGuide {
        private ProductInfo product = new ProductInfoBuilder()
                .setName("default")
                .setTags(TAG_NAMES)
                .build();

        @Override
        public ProductRecordInfo getProduct(RawBaseRecord record) throws GuidingBusinessException {
            return new ProductRecordInfoBuilder()
                    .setProductInfo(product)
                    .setTags(record.getTags())
                    .build();
        }
    }

    private class TestCustomerGuide implements CustomerGuide {
        private Customer customer = new CustomerBuilder()
                .setName("Customer")
                .setCostReference("COSTCENTER")
                .build();


        @Override
        public Customer getCustomer(RawBaseRecord record) throws GuidingBusinessException {
            return customer;
        }
    }
}

