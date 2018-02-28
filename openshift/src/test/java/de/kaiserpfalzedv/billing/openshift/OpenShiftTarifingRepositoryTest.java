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

import java.util.UUID;

import de.kaiserpfalzedv.billing.api.common.CurrencyProvider;
import de.kaiserpfalzedv.billing.api.common.impl.DefaultCurrencyProvider;
import de.kaiserpfalzedv.billing.api.guided.Customer;
import de.kaiserpfalzedv.billing.api.guided.ProductRecordInfo;
import de.kaiserpfalzedv.billing.api.imported.ImportingException;
import de.kaiserpfalzedv.billing.api.rated.NoTarifFoundException;
import de.kaiserpfalzedv.billing.api.rated.Tarif;
import de.kaiserpfalzedv.billing.princeps.api.CustomerBuilder;
import de.kaiserpfalzedv.billing.princeps.api.ProductInfoBuilder;
import de.kaiserpfalzedv.billing.princeps.api.ProductRecordInfoBuilder;
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
public class OpenShiftTarifingRepositoryTest {
    private static final Logger LOG = LoggerFactory.getLogger(OpenShiftTarifingRepositoryTest.class);

    private static final Customer CUSTOMER = new CustomerBuilder()
            .setName("CUSTOMER")
            .setCostReference("COST-REFERENCE")
            .build();

    private OpenShiftTarifingRepository service;


    @Test
    public void shouldGetCorrectTarifWhenCalledWithPODProduct() throws ImportingException, NoTarifFoundException {
        logMethod("product-pod", "Calling with a product of type POD");

        ProductRecordInfo product = new ProductRecordInfoBuilder()
                .setProductInfo(new ProductInfoBuilder()
                                        .setName("POD")
                                        .build())
                .build();

        Tarif result = service.retrieveTarif(CUSTOMER, product);
        LOG.trace("Result: {}", result);

        assertEquals("Product Id does not match!",
                     UUID.fromString("451dd39a-a8bf-4063-acf5-e3c89ad98287"),
                     result.getId());
    }


    @Test
    public void shouldGetCorrectTarifWhenCalledWithCPUProduct() throws ImportingException, NoTarifFoundException {
        logMethod("product-cpu", "Calling with a product of type CPU");

        ProductRecordInfo product = new ProductRecordInfoBuilder()
                .setProductInfo(new ProductInfoBuilder()
                                        .setName("CPU")
                                        .build())
                .build();

        Tarif result = service.retrieveTarif(CUSTOMER, product);
        LOG.trace("Result: {}", result);

        assertEquals("Product Id does not match!",
                     UUID.fromString("e3fc0fc2-96f0-439e-9743-dc5cf750093b"),
                     result.getId());
    }


    @Test
    public void shouldGetCorrectTarifWhenCalledWithMemoryProduct() throws ImportingException, NoTarifFoundException {
        logMethod("product-memory", "Calling with a product of type Memory");

        ProductRecordInfo product = new ProductRecordInfoBuilder()
                .setProductInfo(new ProductInfoBuilder()
                                        .setName("Memory")
                                        .build())
                .build();

        Tarif result = service.retrieveTarif(CUSTOMER, product);
        LOG.trace("Result: {}", result);

        assertEquals("Product Id does not match!",
                     UUID.fromString("eb3f8469-89b2-4094-8014-a471f527699d"),
                     result.getId());
    }


    @Test
    public void shouldGetCorrectTarifWhenCalledWithNetworkProduct() throws ImportingException, NoTarifFoundException {
        logMethod("product-network", "Calling with a product of type Network");

        ProductRecordInfo product = new ProductRecordInfoBuilder()
                .setProductInfo(new ProductInfoBuilder()
                                        .setName("Network")
                                        .build())
                .build();

        Tarif result = service.retrieveTarif(CUSTOMER, product);
        LOG.trace("Result: {}", result);

        assertEquals("Product Id does not match!",
                     UUID.fromString("ddf90e50-e906-4260-99e6-9aab4f7c1fe7"),
                     result.getId());
    }


    @Test
    public void shouldGetCorrectTarifWhenCalledWithStorageProduct() throws ImportingException, NoTarifFoundException {
        logMethod("product-storage", "Calling with a product of type Storage");

        ProductRecordInfo product = new ProductRecordInfoBuilder()
                .setProductInfo(new ProductInfoBuilder()
                                        .setName("Storage")
                                        .build())
                .build();

        Tarif result = service.retrieveTarif(CUSTOMER, product);
        LOG.trace("Result: {}", result);

        assertEquals("Product Id does not match!",
                     UUID.fromString("86cf3529-9ee5-4f0f-897e-2c31ff7cc545"),
                     result.getId());
    }

    @Test
    public void shouldThrowANoTarifFoundExceptionWhenNoMatchingTarifIsFound() {
        logMethod("no-tarif-found", "Calling with product information with no tarif");

        ProductRecordInfo product = new ProductRecordInfoBuilder()
                .setProductInfo(new ProductInfoBuilder()
                                        .setName("No matching product")
                                        .build())
                .build();

        try {
            service.retrieveTarif(CUSTOMER, product);
            fail("The NoTarifFoundException should have been thrown!");
        } catch (NoTarifFoundException e) {
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
        CurrencyProvider currencyProvider = new DefaultCurrencyProvider();
        service = new OpenShiftTarifingRepository(currencyProvider);
        service.init();
    }

    @BeforeClass
    public static void setUpMDC() {
        MDC.put("test", OpenShiftTarifingRepository.class.getSimpleName());
    }

    @AfterClass
    public static void tearDown() {
        MDC.remove("id");
        MDC.remove("test");
    }
}
