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
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.UUID;

import javax.money.Monetary;

import de.kaiserpfalzedv.billing.api.cdr.CallDataRecord;
import de.kaiserpfalzedv.billing.api.cdr.CallDataRecordAttachment;
import de.kaiserpfalzedv.billing.api.imported.ImportingException;
import de.kaiserpfalzedv.billing.quod.CallDataRecordAttachmentBuilder;
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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-17
 */
public class CallDataRecordAttachmentTest {
    private static final Logger LOG = LoggerFactory.getLogger(CallDataRecordAttachmentTest.class);


    private static final UUID ID = UUID.randomUUID();
    private static final String TITLE = "Beschreibung des Eintrags";
    private static final OffsetDateTime TIMESTAMP = OffsetDateTime.now(ZoneOffset.UTC);

    private static final ArrayList<CallDataRecord> RECORDS = new ArrayList<>(5);
    static {
        RECORDS.add(
                new CallDataRecordBuilder()
                        .withDescription("Description")
                        .withTarifName("Tarif Name")
                        .withTarifRate(Money.of(0L, Monetary.getCurrency("EUR")))
                        .withTarifUnit("Unit")
                        .withMeteredTimestamp(OffsetDateTime.now(ZoneOffset.UTC))
                        .build()
        );
        RECORDS.add(
                new CallDataRecordBuilder()
                        .withDescription("Description")
                        .withTarifName("Tarif Name")
                        .withTarifRate(Money.of(0L, Monetary.getCurrency("EUR")))
                        .withTarifUnit("Unit")
                        .withMeteredTimestamp(OffsetDateTime.now(ZoneOffset.UTC))
                        .build()
        );
        RECORDS.add(
                new CallDataRecordBuilder()
                        .withDescription("Description")
                        .withTarifName("Tarif Name")
                        .withTarifRate(Money.of(0L, Monetary.getCurrency("EUR")))
                        .withTarifUnit("Unit")
                        .withMeteredTimestamp(OffsetDateTime.now(ZoneOffset.UTC))
                        .build()
        );
    }

    private CallDataRecordAttachmentBuilder service;


    @Test
    public void shouldCreateAValidRecordWhenAllDataIsSpecified() {
        logMethod("full-data", "Creating a new attachment with all data.");

        CallDataRecordAttachment result = service
                .withId(ID)
                .withTitle(TITLE)
                .withTimestamp(TIMESTAMP)
                .withRecords(RECORDS)
                .build();
        LOG.trace("Result: {}", result);


        assertEquals("ID does not match!", ID, result.getId());
        assertEquals("Title does not match!", TITLE, result.getTitle());
        assertEquals("Timestamp does not match!", TIMESTAMP, result.getTimestamp());

        assertArrayEquals("Records do not match!",
                          RECORDS.toArray(new CallDataRecord[0]), result.getRecords().toArray(new CallDataRecord[0]));
    }

    @Test
    public void shouldCreateAValidRecordWhenOnlyMinimalDataIsSpecified() {
        logMethod("minimum-data", "Creating a new crecord with minimum data.");

        CallDataRecordAttachment result = service.build();
        LOG.trace("Result: {}", result);
    }

    @Test
    public void shouldCreateTheAttachmentWhenRecordsAreAdded() {
        logMethod("minimum-data", "Creating a new crecord with minimum data.");

        for (CallDataRecord record : RECORDS) {
            service.addRecord(record);
        }

        CallDataRecordAttachment result = service.build();
        LOG.trace("Result: {}", result);

        assertArrayEquals("Records do not match!",
                          RECORDS.toArray(new CallDataRecord[0]), result.getRecords().toArray(new CallDataRecord[0]));
    }


    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @Before
    public void setUp() throws FileNotFoundException, ImportingException {
        service = new CallDataRecordAttachmentBuilder();
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
