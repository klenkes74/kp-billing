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

package de.kaiserpfalzedv.billing.openshift.test;

import java.math.BigDecimal;
import java.util.HashMap;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

import de.kaiserpfalzedv.billing.api.guided.Customer;
import de.kaiserpfalzedv.billing.api.guided.ProductRecordInfo;
import de.kaiserpfalzedv.billing.api.rated.RatedMeteredRecord;
import de.kaiserpfalzedv.billing.api.rated.Tarif;
import de.kaiserpfalzedv.billing.openshift.OpenShiftCDRGenerator;
import de.kaiserpfalzedv.billing.openshift.OpenShiftCallDataRecordImpl;
import de.kaiserpfalzedv.billing.openshift.OpenShiftProductRepository;
import de.kaiserpfalzedv.billing.princeps.api.CustomerBuilder;
import de.kaiserpfalzedv.billing.princeps.api.ProductInfoBuilder;
import de.kaiserpfalzedv.billing.princeps.api.ProductRecordInfoBuilder;
import de.kaiserpfalzedv.billing.ratio.RatedRecordBuilder;
import de.kaiserpfalzedv.billing.ratio.api.TarifBuilder;
import org.javamoney.moneta.Money;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static org.junit.Assert.assertEquals;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-19
 */
public class OpenShiftCDRGeneratorTest {
    private static final Logger LOG = LoggerFactory.getLogger(OpenShiftCDRGeneratorTest.class);

    private OpenShiftCDRGenerator service;

    private static final Customer CUSTOMER = new CustomerBuilder()
            .setName("CUSTOMER")
            .setCostReference("COST-REFERENCE")
            .build();

    private static final ProductRecordInfo PRODUCT = new ProductRecordInfoBuilder()
            .setProductInfo(new ProductInfoBuilder()
                                    .setName("POD")
                                    .build())
            .build();

    private static final MonetaryAmount RATE = Money.of(10, Monetary.getCurrency("EUR"));
    private static final MonetaryAmount AMOUNT = Money.of(10, Monetary.getCurrency("EUR"));

    private static final Tarif TARIF = new TarifBuilder()
            .setName("POD")
            .setRate(RATE)
            .setUnit("pcs")
            .build();

    @Test
    public void shouldReturnAValidCDRWhenARatedTimedRecordIsGiven() {
        logMethod("valid-cdr", "Generate a valid CDR for OpenShift data");

        HashMap<String, String> tags = new HashMap<>();
        tags.put("cluster", "abbot1");
        tags.put("project", "billing");
        tags.put("pod", "quod-4fs2a");

        RatedMeteredRecord record = new RatedRecordBuilder<RatedMeteredRecord>()
                .setCustomer(CUSTOMER)
                .setProductInfo(PRODUCT)
                .setTarif(TARIF)
                .setMeteredValue(BigDecimal.ONE)
                .setTags(tags)
                .build();

        OpenShiftCallDataRecordImpl result = service.generate(record);
        LOG.trace("Result: {}", result);

        assertEquals("The description is not correctly generated!",
                     "POD: quod-4fs2a", result.getDescription());
        assertEquals("The amount does not match!", AMOUNT, result.getAmount());
    }


    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @Before
    public void setUp() {
        service = new OpenShiftCDRGenerator();
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
