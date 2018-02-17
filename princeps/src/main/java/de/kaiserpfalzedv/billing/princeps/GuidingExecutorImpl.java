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

package de.kaiserpfalzedv.billing.princeps;

import de.kaiserpfalzedv.billing.api.guided.CustomerGuide;
import de.kaiserpfalzedv.billing.api.guided.GuidedMeteredRecord;
import de.kaiserpfalzedv.billing.api.guided.GuidedTimedRecord;
import de.kaiserpfalzedv.billing.api.guided.GuidingBusinessExeption;
import de.kaiserpfalzedv.billing.api.guided.GuidingExecutor;
import de.kaiserpfalzedv.billing.api.guided.ProductGuide;
import de.kaiserpfalzedv.billing.api.imported.RawMeteredRecord;
import de.kaiserpfalzedv.billing.api.imported.RawTimedRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-16
 */
public class GuidingExecutorImpl implements GuidingExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(GuidingExecutorImpl.class);

    @Override
    public GuidedMeteredRecord executeMeteredRecord(
            final RawMeteredRecord record,
            final ProductGuide productGuide,
            final CustomerGuide customerGuide
    ) throws GuidingBusinessExeption {
        MDC.put("record-id", record.getId().toString());
        MDC.put("metering-id", record.getMeteringId());

        LOG.debug("Guiding record: {}", record);
        LOG.trace("productGuide={}, customerGuide={}", productGuide, customerGuide);

        GuidedMeteredRecord result = new GuidedRecordBuilder<GuidedMeteredRecord>()
                .setCustomer(customerGuide.guide(record))
                .setProductInfo(productGuide.guide(record))
                .setId(record.getId())
                .setImportedDate(record.getImportedDate())
                .setRecordedDate(record.getRecordedDate())
                .setValueDate(record.getValueDate())
                .setMeteringId(record.getMeteringId())
                .setMeteredTimestamp(record.getMeteredTimestamp())
                .setMeteredDuration(record.getMeteredDuration())
                .setMeteredValue(record.getMeteredValue())
                .build();

        LOG.trace("GuidedMeteredRecord: {}", result);
        MDC.remove("record-id");
        MDC.remove("metering-id");
        return result;
    }

    @Override
    public GuidedTimedRecord executeTimedRecord(
            final RawTimedRecord record,
            final ProductGuide productGuide,
            final CustomerGuide customerGuide
    ) throws GuidingBusinessExeption {
        MDC.put("record-id", record.getId().toString());
        MDC.put("metering-id", record.getMeteringId());

        LOG.debug("Guiding record: {}", record);
        LOG.trace("productGuide={}, customerGuide={}", productGuide, customerGuide);

        GuidedTimedRecord result = new GuidedRecordBuilder<GuidedTimedRecord>()
                .setCustomer(customerGuide.guide(record))
                .setProductInfo(productGuide.guide(record))
                .setId(record.getId())
                .setImportedDate(record.getImportedDate())
                .setRecordedDate(record.getRecordedDate())
                .setValueDate(record.getValueDate())
                .setMeteringId(record.getMeteringId())
                .setMeteredTimestamp(record.getMeteredTimestamp())
                .setMeteredDuration(record.getMeteredDuration())
                .build();

        LOG.trace("GuidedTimedRecord: {}", result);
        MDC.remove("record-id");
        MDC.remove("metering-id");
        return result;
    }
}
