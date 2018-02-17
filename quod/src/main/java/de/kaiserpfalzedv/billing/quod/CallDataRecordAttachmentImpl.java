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
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.api.cdr.CallDataRecord;
import de.kaiserpfalzedv.billing.api.cdr.CallDataRecordAttachment;
import de.kaiserpfalzedv.billing.api.common.impl.IdentifiableImpl;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * This is the attachment containing the {@link CallDataRecord}s.
 *
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-17
 */
public class CallDataRecordAttachmentImpl extends IdentifiableImpl implements CallDataRecordAttachment {
    private static final long serialVersionUID = -744880371203483028L;

    /** The title of the CDR Attachment */
    private final String title;

    /** The timestamp of this attachment generation */
    private final OffsetDateTime timestamp;

    /** The CDRs on this attachment */
    private final ArrayList<CallDataRecord> records = new ArrayList<>();

    CallDataRecordAttachmentImpl(
            @NotNull final UUID id,
            @NotNull final String title,
            @NotNull final OffsetDateTime timestamp,
            @NotNull final List<CallDataRecord> records
    ) {
        super(id);

        this.title = title;
        this.timestamp = timestamp;
        this.records.addAll(records);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    
    @Override
    public List<CallDataRecord> getRecords() {
        return records;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("title", title)
                .append("timestamp", timestamp)
                .append("records", records.size())
                .toString();
    }
}
