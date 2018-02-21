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

package de.kaiserpfalzedv.billing.invectio.csv.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import de.kaiserpfalzedv.billing.api.imported.ImportingException;
import de.kaiserpfalzedv.billing.api.imported.RawBaseRecord;
import de.kaiserpfalzedv.billing.invectio.csv.CSVImporter;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-15
 */
public class CSVImporterTest {
    private static final Logger LOG = LoggerFactory.getLogger(CSVImporterTest.class);

    private static final String CSV_FILE_NAME = "./target/test-classes/libellum-9xfd3.csv";

    private CSVImporter service;
    private Reader csvFile;



    @Test
    public void shouldGenerateFiveEntriesWhenCalledWithFileLbellum() throws ImportingException {
        logMethod("simple-read", "Read simple file without errors: {}", CSV_FILE_NAME);

        List<? extends RawBaseRecord> result = service.execute(csvFile);
        LOG.trace("Result: {}", result);

        Assert.assertEquals("Wrong number of records!", 8, result.size());
    }



    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @Before
    public void setUp() throws FileNotFoundException {
        service = new CSVImporter();

        csvFile = new FileReader(new File(CSV_FILE_NAME));
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
}
