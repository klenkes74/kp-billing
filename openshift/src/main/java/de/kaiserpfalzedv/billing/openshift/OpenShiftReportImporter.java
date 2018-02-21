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

import java.io.StringReader;
import java.util.List;

import javax.inject.Inject;

import de.kaiserpfalzedv.billing.api.imported.ImporterService;
import de.kaiserpfalzedv.billing.api.imported.ImportingException;
import de.kaiserpfalzedv.billing.api.imported.RawBaseRecord;
import de.kaiserpfalzedv.billing.openshift.api.ImportOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-17
 */
public class OpenShiftReportImporter {
    private static final Logger LOG = LoggerFactory.getLogger(OpenShiftReportImporter.class);

    private ImporterService importer;


    @Inject
    public OpenShiftReportImporter(
            final ImporterService importer
    ) {
        this.importer = importer;
    }


    public List<? extends RawBaseRecord> execute(final ImportOrder order) throws ImportingException {
        StringReader reader = new StringReader(order.getText());
        List<? extends RawBaseRecord> result = importer.execute(reader);


        LOG.info("Importing {} billing records.", result.size());
        return result;
    }
}
