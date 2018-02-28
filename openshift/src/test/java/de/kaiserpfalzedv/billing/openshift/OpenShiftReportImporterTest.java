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

import java.io.Reader;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.kaiserpfalzedv.billing.api.imported.ImporterService;
import de.kaiserpfalzedv.billing.api.imported.ImportingException;
import de.kaiserpfalzedv.billing.api.imported.RawBaseRecord;
import de.kaiserpfalzedv.billing.openshift.api.ImportOrder;
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
 * @since 2018-02-17
 */
public class OpenShiftReportImporterTest {
    private static final Logger LOG = LoggerFactory.getLogger(OpenShiftReportImporterTest.class);

    private OpenShiftReportImporter service;


    @Test
    public void shouldCreateEmptyListWhenCalledWithData() throws ImportingException {
        logMethod("simple-call", "Simple call of importer.");

        ImportOrder order = new ImportOrder();
        order.setId(UUID.randomUUID());
        order.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC));
        order.setText("");

        List<? extends RawBaseRecord> result = service.execute(order);

        assertEquals(0, result.size());
    }


    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @Before
    public void setUp() {
        service = new OpenShiftReportImporter(new FakeImporter());
    }

    @BeforeClass
    public static void setUpMDC() {
        MDC.put("test", OpenShiftReportImporter.class.getSimpleName());
    }

    @AfterClass
    public static void tearDown() {
        MDC.remove("id");
        MDC.remove("test");
    }

    
    private class FakeImporter implements ImporterService {
        @Override
        public List<? extends RawBaseRecord> execute(Reader reader) throws ImportingException {
            return new ArrayList<>();
        }
    }
}
