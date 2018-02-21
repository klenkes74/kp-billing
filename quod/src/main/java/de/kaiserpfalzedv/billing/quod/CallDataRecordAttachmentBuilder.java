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

package de.kaiserpfalzedv.billing.quod;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.api.cdr.CallDataRecord;
import de.kaiserpfalzedv.billing.api.cdr.CallDataRecordAttachment;
import org.apache.commons.lang3.builder.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.time.ZoneOffset.UTC;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-17
 */
public final class CallDataRecordAttachmentBuilder implements Builder<CallDataRecordAttachment> {
    private static final Logger LOG = LoggerFactory.getLogger(CallDataRecordAttachmentBuilder.class);

    private UUID id;
    private String title;
    private OffsetDateTime timestamp;
    private final ArrayList<CallDataRecord> records = new ArrayList<>();


    public CallDataRecordAttachment build() {
        validate();
        defaults();

        CallDataRecordAttachment result = new CallDataRecordAttachmentImpl(id, title, timestamp, records);
        LOG.trace("Created CDR Attachment: {}", result);

        reset();
        return result;
    }

    private void validate() {
        if (records.isEmpty()) {
            LOG.info("Generating CallDataRecordAttachment without records.");
        }
    }

    private void defaults() {
        if (id == null) {
            id = UUID.randomUUID();
        }

        if (timestamp == null) {
            timestamp = OffsetDateTime.now(UTC);
        }

        if (isBlank(title)) {
            title = "CDR Attachment " + id;
        }
    }

    private void reset() {
        id = null;
        title = null;
        timestamp = null;
        records.clear();

        LOG.debug("Resetted builder: {}", this);
    }


    public CallDataRecordAttachmentBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public CallDataRecordAttachmentBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public CallDataRecordAttachmentBuilder withTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public CallDataRecordAttachmentBuilder withRecords(@NotNull final Collection<CallDataRecord> records) {
        this.records.clear();

        if (records != null) {
            this.records.addAll(records);
        }
        
        return this;
    }

    public CallDataRecordAttachmentBuilder addRecord(@NotNull final CallDataRecord record) {
        records.add(record);
        return this;
    }
}
