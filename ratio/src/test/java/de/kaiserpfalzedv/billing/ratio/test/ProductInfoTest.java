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

import java.util.ArrayList;
import java.util.UUID;

import de.kaiserpfalzedv.billing.api.guided.ProductInfo;
import de.kaiserpfalzedv.billing.princeps.api.ProductInfoBuilder;
import org.junit.After;
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
 * @since 2018-02-10
 */
public class ProductInfoTest {
    private static final Logger LOG = LoggerFactory.getLogger(ProductInfoTest.class);

    private static final UUID ID = UUID.randomUUID();
    private static final String NAME = "unit test";
    private static final ArrayList<String> TAG_NAMES = new ArrayList<>(4);
    static {
        TAG_NAMES.add("cluster");
        TAG_NAMES.add("project");
        TAG_NAMES.add("pod");
        TAG_NAMES.add("customer");
    }

    private static final ProductInfo PRODUCT_INFO = new ProductInfoBuilder()
            .setId(ID)
            .setName(NAME)
            .setTags(TAG_NAMES)
            .build();


    private ProductInfoBuilder service;

    @BeforeClass
    public static void setUpClass() {
        MDC.put("test", "ProductInfoTest");

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
    public void generateSimpleProductInfo() {
        logMethod("productinfo", "Testing a simple productinfo");

        ProductInfo result = service
                .setName(NAME)
                .build();
        LOG.debug("result: {}", result);

        assertNotNull("The id should default to a random UUID", result.getId());
        assertEquals("Name does not match", NAME, result.getName());
        assertArrayEquals("Tags don't match", new ArrayList<String>().toArray(), result.getTags().toArray());
    }

    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @Test
    public void copyProductInfo() {
        logMethod("copy-productinfo", "Copying the product info: {}", PRODUCT_INFO);

        ProductInfo result = service.copy(PRODUCT_INFO).build();
        LOG.debug("result: {}", result);

        assertEquals("ID does not match", ID, result.getId());
        assertEquals("Name does not match", NAME, result.getName());
        assertArrayEquals("Tags do not match", TAG_NAMES.toArray(), result.getTags().toArray());
    }

    @Test(timeout = 80L)
    public void runtimeTest() {
        logMethod("runtime-verification", "checking the runtime of the tarif builder ...");

        for (int i = 0; i < 1000; i++) {
            service
                    .setName(NAME)
                    .build();
        }
    }

    @Before
    public void setUpService() {
        service = new ProductInfoBuilder();
    }

    @After
    public void tearDownService() {
        MDC.remove("id");
    }
}
