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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import de.kaiserpfalzedv.billing.api.guided.ProductInfo;
import de.kaiserpfalzedv.billing.api.guided.ProductRecordInfo;
import de.kaiserpfalzedv.billing.princeps.api.ProductInfoBuilder;
import de.kaiserpfalzedv.billing.princeps.api.ProductRecordInfoBuilder;
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

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class ProductRecordInfoTest {
    private static final Logger LOG = LoggerFactory.getLogger(ProductRecordInfoTest.class);

    private static final UUID PRODUCT_ID = UUID.randomUUID();

    private static final ArrayList<String> TAG_NAMES = new ArrayList<>(4);
    static {
        TAG_NAMES.add("cluster");
        TAG_NAMES.add("project");
        TAG_NAMES.add("pod");
        TAG_NAMES.add("customer");
    }

    private static final ProductInfo PRODUCT_INFO = new ProductInfoBuilder()
            .setId(PRODUCT_ID)
            .setName("Product")
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

    private ProductRecordInfoBuilder service;

    @BeforeClass
    public static void setUpClass() {
        MDC.put("test", "ProductRecordInfoTest");

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
    public void generateSimpleProductRecordInfo() {
        logMethod("productrecordinfo", "Testing a simple productrecordinfo");

        ProductRecordInfo result = service
                .setProductInfo(PRODUCT_INFO)
                .setTags(TAGS)
                .build();
        LOG.debug("result: {}", result);

        assertEquals("Product does not match", PRODUCT_INFO, result.getProductInfo());
        assertEquals("The tags do not match", TAGS, result.getTags());
    }

    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @Test
    public void copyProductRecordInfo() {
        logMethod("copy-productrecordinfo", "Copying the product record info: {}", PRODUCT_INFO);

        ProductRecordInfo result = service.copy(PRODUCT_RECORD_INFO).build();
        LOG.debug("result: {}", result);

        assertEquals("Product info does not match", PRODUCT_INFO, result.getProductInfo());
        assertEquals("The tags do not match", TAGS, result.getTags());
    }

    @Test(timeout = 80L)
    public void runtimeTest() {
        logMethod("runtime-verification", "checking the runtime of the tarif builder ...");

        for (int i = 0; i < 1000; i++) {
            service
                    .setProductInfo(PRODUCT_INFO)
                    .setTags(TAGS)
                    .build();
        }
    }

    @Before
    public void setUpService() {
        service = new ProductRecordInfoBuilder();
    }

    @After
    public void tearDownService() {
        MDC.remove("id");
    }
}
