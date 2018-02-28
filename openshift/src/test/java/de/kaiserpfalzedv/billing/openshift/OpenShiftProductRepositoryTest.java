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

package de.kaiserpfalzedv.billing.openshift;

import java.util.HashMap;
import java.util.UUID;

import de.kaiserpfalzedv.billing.api.guided.Customer;
import de.kaiserpfalzedv.billing.api.guided.NoProductFoundException;
import de.kaiserpfalzedv.billing.api.guided.ProductInfo;
import de.kaiserpfalzedv.billing.api.imported.ImportingException;
import de.kaiserpfalzedv.billing.princeps.api.CustomerBuilder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-18
 */
public class OpenShiftProductRepositoryTest {
    private static final Logger LOG = LoggerFactory.getLogger(OpenShiftProductRepositoryTest.class);

    private static final Customer CUSTOMER = new CustomerBuilder()
            .setName("CUSTOMER")
            .setCostReference("COST-REFERENCE")
            .build();

    private OpenShiftProductRepository service;

    private final HashMap<String, String> tags = new HashMap<>();


    @Test
    public void shouldGetCorrectProductWhenCalledWithPODProduct() throws ImportingException, NoProductFoundException {
        logMethod("product-pod", "Calling with a product of type POD");

        tags.put("product", "POD");

        ProductInfo result = service.retrieveProduct(tags);
        LOG.trace("Result: {}", result);

        assertEquals("Product Id does not match!",
                     UUID.fromString("11493085-2cb6-4c34-adf9-f80fb798ff90"),
                     result.getId());
    }


    @Test
    public void shouldGetCorrectProductWhenCalledWithCPUProduct() throws ImportingException, NoProductFoundException {
        logMethod("product-cpu", "Calling with a product of type CPU");

        tags.put("product", "CPU");

        ProductInfo result = service.retrieveProduct(tags);
        LOG.trace("Result: {}", result);

        assertEquals("Product Id does not match!",
                     UUID.fromString("cf24d806-f303-4c69-85c7-67f5f93a8416"),
                     result.getId());
    }


    @Test
    public void shouldGetCorrectProductWhenCalledWithMemoryProduct() throws ImportingException, NoProductFoundException {
        logMethod("product-memory", "Calling with a product of type Memory");

        tags.put("product", "Memory");

        ProductInfo result = service.retrieveProduct(tags);
        LOG.trace("Result: {}", result);

        assertEquals("Product Id does not match!",
                     UUID.fromString("79b8b0d3-45d2-465f-b147-3a9ed13e5970"),
                     result.getId());
    }


    @Test
    public void shouldGetCorrectProductWhenCalledWithNetworkProduct() throws ImportingException, NoProductFoundException {
        logMethod("product-network", "Calling with a product of type Network");

        tags.put("product", "Network");

        ProductInfo result = service.retrieveProduct(tags);
        LOG.trace("Result: {}", result);

        assertEquals("Product Id does not match!",
                     UUID.fromString("4ebd7650-7424-4fa0-980b-0e930cbe7866"),
                     result.getId());
    }


    @Test
    public void shouldGetCorrectProductWhenCalledWithStorageProduct() throws ImportingException, NoProductFoundException {
        logMethod("product-storage", "Calling with a product of type Storage");

        tags.put("product", "Storage");

        ProductInfo result = service.retrieveProduct(tags);
        LOG.trace("Result: {}", result);

        assertEquals("Product Id does not match!",
                     UUID.fromString("1016848d-cf44-4cb1-b214-1d78cec2d3db"),
                     result.getId());
    }


    @Test
    public void shouldThrowANoTarifFoundExceptionWhenNoMatchingTarifIsFound() {
        logMethod("no-tarif-found", "Calling with product information with no tarif");

        tags.put("product", "No matching product");

        try {
            service.retrieveProduct(tags);
            fail("The NoTarifFoundException should have been thrown!");
        } catch (NoProductFoundException e) {
            LOG.trace("Result: {}", e.getMessage());

            // everything is fine.
        }
    }


    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @Before
    public void setUp() {
        service = new OpenShiftProductRepository();
        tags.clear();
    }

    @BeforeClass
    public static void setUpMDC() {
        MDC.put("test", OpenShiftProductRepository.class.getSimpleName());
    }

    @AfterClass
    public static void tearDown() {
        MDC.remove("id");
        MDC.remove("test");
    }
}
