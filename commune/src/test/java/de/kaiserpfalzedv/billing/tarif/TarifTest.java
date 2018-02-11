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

package de.kaiserpfalzedv.billing.tarif;

import java.math.BigDecimal;
import java.util.UUID;

import javax.money.MonetaryAmount;
import javax.xml.registry.JAXRException;

import de.kaiserpfalzedv.billing.tarif.impl.TarifBuilder;
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
import static org.junit.Assert.assertNotNull;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
@SuppressWarnings("ALL")
public class TarifTest {
    private static final Logger LOG = LoggerFactory.getLogger(TarifTest.class);

    private static final UUID ID = UUID.randomUUID();
    private static final String TARIF_NAME = "unit test";
    private static final String UNIT = "mCore/h";
    private static final BigDecimal UNIT_DIVISOR = BigDecimal.TEN;
    private static final BigDecimal DEFAULT_UNIT_DIVISOR = BigDecimal.ONE;
    private static final MonetaryAmount RATE = new MoneyAmountBuilder().setNumber(0.01d).setCurrency("EUR").create();
    private static final Tarif TARIF = new TarifBuilder()
            .setId(ID)
            .setTarifName(TARIF_NAME)
            .setUnit(UNIT)
            .setUnitDivisor(UNIT_DIVISOR)
            .setRate(RATE)
            .build();


    private TarifBuilder service;

    @BeforeClass
    public static void setUpClass() {
        MDC.put("test", "TarifTest");

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
    public void generateSimpleEmail() throws JAXRException {
        logMethod("tarif", "Testing a simple tarif");

        Tarif result = service
                .setTarifName(TARIF_NAME)
                .setUnit(UNIT)
                .setRate(RATE)
                .build();
        LOG.debug("result: {}", result);

        assertNotNull("The id should default to a random UUID", result.getId());
        assertEquals("Name does not match", TARIF_NAME, result.getTarifName());
        assertEquals("Unit does not match", UNIT, result.getUnit());
        assertEquals("Unit divisor does not match default divisor", DEFAULT_UNIT_DIVISOR, result.getUnitDivisor());
        assertEquals("Rate does not match", RATE, result.getRate());
    }

    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @Test
    public void copyEmail() throws JAXRException {
        logMethod("copy-tarif", "Copying the tarif: {}", TARIF);

        Tarif result = service.copy(TARIF).build();
        LOG.debug("result: {}", result);

        assertEquals("ID does not match", ID, result.getId());
        assertEquals("Name does not match", TARIF_NAME, result.getTarifName());
        assertEquals("Unit does not match", UNIT, result.getUnit());
        assertEquals("Unit divisor does not match divisor", UNIT_DIVISOR, result.getUnitDivisor());
        assertEquals("Rate does not match", RATE, result.getRate());
    }

    @Test(timeout = 80L)
    public void runtimeTest() {
        logMethod("runtime-verification", "checking the runtime of the tarif builder ...");

        for (int i = 0; i < 1000; i++) {
            service
                    .setTarifName(TARIF_NAME)
                    .setUnit(UNIT)
                    .setRate(RATE)
                    .build();
            service
                    .setTarifName(TARIF_NAME)
                    .setUnit(UNIT)
                    .setRate(RATE)
                    .build();
        }
    }

    @Before
    public void setUpService() {
        service = new TarifBuilder();
    }

    @After
    public void tearDownService() {
        MDC.remove("id");
    }
}
