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

package de.kaiserpfalzedv.billing.quod.test;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

import de.kaiserpfalzedv.billing.api.cdr.CallDataRecord;
import de.kaiserpfalzedv.billing.api.common.BuilderException;
import de.kaiserpfalzedv.billing.api.common.impl.DefaultCurrencyProvider;
import de.kaiserpfalzedv.billing.api.imported.ImportingException;
import de.kaiserpfalzedv.billing.quod.CallDataRecordBuilder;
import org.javamoney.moneta.Money;
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
import static org.junit.Assert.fail;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-17
 */
public class CallDataRecordTest {
    private static final Logger LOG = LoggerFactory.getLogger(CallDataRecordTest.class);


    private static final UUID ID = UUID.randomUUID();
    private static final String DESCRIPTION = "Beschreibung des Eintrags";

    private static final String TARIF_NAME = "CPU";
    private static final MonetaryAmount TARIF_RATE = Money.of(0.5, Monetary.getCurrency("EUR"));
    private static final String TARIF_UNIT = "Core";
    private static final BigDecimal TARIF_UNIT_DIVISOR = BigDecimal.valueOf(1000L);

    private static final OffsetDateTime METERED_TIMESTAMP = OffsetDateTime.now(ZoneOffset.UTC).minusMinutes(15L);
    private static final Duration METERED_DURATION = Duration.ofMinutes(15L);
    private static final BigDecimal METERED_VALUE = BigDecimal.valueOf(1000L);
    private static final MonetaryAmount AMOUNT = Money.of(0.125, Monetary.getCurrency("EUR"));


    private CallDataRecordBuilder service;


    @Test
    public void shouldCreateAValidRecordWhenAllDataIsSpecified() {
        logMethod("full-data", "Creating a new crecord with all data.");

        CallDataRecord result = service
                .withId(ID)
                .withDescription(DESCRIPTION)

                .withTarifName(TARIF_NAME)
                .withTarifRate(TARIF_RATE)
                .withTarifUnit(TARIF_UNIT)
                .withTarifUnitDivisor(TARIF_UNIT_DIVISOR)

                .withMeteredTimestamp(METERED_TIMESTAMP)
                .withMeteredDuration(METERED_DURATION)
                .withMeteredValue(METERED_VALUE)

                .withAmount(AMOUNT)
                .withCurrencyProvider(new DefaultCurrencyProvider())
                .build();
        LOG.trace("Result: {}", result);


        assertEquals("ID does not match!", ID, result.getId());
        assertEquals("Description does not match!", DESCRIPTION, result.getDescription());

        assertEquals("Tarif name does not match!", TARIF_NAME, result.getTarifName());
        assertEquals("Tarif rate does not match!", TARIF_RATE, result.getTarifRate());
        assertEquals("Tarif unit does not match!", TARIF_UNIT, result.getTarifUnit());
        assertEquals("Tarif unit divisor does not match!", TARIF_UNIT_DIVISOR, result.getTarifUnitDivisor());

        assertEquals("Metered timestamp does not match!", METERED_TIMESTAMP, result.getMeteredTimestamp());
        assertEquals("Metered duration does not match!", METERED_DURATION, result.getMeteredDuration());
        assertEquals("Metered value does not match!", METERED_VALUE, result.getMeteredValue());

        assertEquals("Amount does not match!", AMOUNT, result.getAmount());
    }

    @Test
    public void shouldCreateAValidRecordWhenOnlyMinimalDataIsSpecified() {
        logMethod("minimum-data", "Creating a new crecord with minimum data.");

        CallDataRecord result = service
                .withDescription(DESCRIPTION)

                .withTarifName(TARIF_NAME)
                .withTarifRate(TARIF_RATE)
                .withTarifUnit(TARIF_UNIT)

                .withMeteredTimestamp(METERED_TIMESTAMP)
                .build();
        LOG.trace("Result: {}", result);


        assertNotNull("ID is not set!", result.getId());
        assertEquals("Description does not match!", DESCRIPTION, result.getDescription());

        assertEquals("Tarif name does not match!", TARIF_NAME, result.getTarifName());
        assertEquals("Tarif rate does not match!", TARIF_RATE, result.getTarifRate());
        assertEquals("Tarif unit does not match!", TARIF_UNIT, result.getTarifUnit());
        assertEquals("Tarif unit divisor does not match!", BigDecimal.ONE, result.getTarifUnitDivisor());

        assertEquals("Metered timestamp does not match!", METERED_TIMESTAMP, result.getMeteredTimestamp());
        assertEquals("Metered duration does not match!", Duration.ZERO, result.getMeteredDuration());
        assertEquals("Metered value does not match!", BigDecimal.ZERO, result.getMeteredValue());

        assertEquals("Amount does not match!",
                     Money.of(0L, new DefaultCurrencyProvider().getCurrency()), result.getAmount());
    }

    @Test
    public void shouldThrowBuilderExceptionWith5FailuresWhenNoDataIsSpecified() {
        logMethod("no-data", "Creating a record without any data.");

        try {
            service.build();

            fail("BuilderException should have been thrown!");
        } catch (BuilderException e) {
            LOG.trace("Result: {}", e.getMessage(), e);

            assertEquals("There should be 5 failures!", 5, e.getFailures().size());
        }
    }


    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @Before
    public void setUp() throws FileNotFoundException, ImportingException {
        service = new CallDataRecordBuilder();
    }

    @BeforeClass
    public static void setUpMDC() {
        MDC.put("test", CallDataRecord.class.getSimpleName());

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
}
