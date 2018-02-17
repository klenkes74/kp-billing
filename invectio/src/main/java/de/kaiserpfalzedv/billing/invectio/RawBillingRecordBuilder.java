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

package de.kaiserpfalzedv.billing.invectio;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.api.imported.RawBaseRecord;
import de.kaiserpfalzedv.billing.api.imported.RawMeteredRecord;
import org.apache.commons.lang3.builder.Builder;

import static java.time.ZoneOffset.UTC;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class RawBillingRecordBuilder<T extends RawBaseRecord> implements Builder<T> {

    /**
     * ID of this billing record.
     */
    private UUID id;

    /**
     * Date this record will be valued at (this is the date determining the pricing within the tarif)
     */
    private OffsetDateTime valueDate;

    /**
     * The date this record has been recorded at the metering unit. If no date is given defaults to
     * {@link #importedDate}
     */
    private OffsetDateTime recordedDate;

    /**
     * The date and time this record has been imported into the billing system.
     */
    private OffsetDateTime importedDate;

    /**
     * Id from the metering system for this record.
     */
    private String meteringId;

    /**
     * The value metered (for some product lines this may be zero, since the duration is the metered value).
     */
    private BigDecimal meteredValue;

    /**
     * The start date of the billing event. May be the start of a call or the start of the hour of billed CPU usage.
     */
    private OffsetDateTime meteredTimestamp;

    /**
     * The duration of the billed event. May be the call duration or the period metered.
     */
    private Duration meteredDuration;

    private final HashMap<String, String> tags = new HashMap<>();


    @SuppressWarnings({"unchecked", "deprecation"})
    @Override
    public T build() {
        defaults();
        validate();

        T result;
        if (meteredValue != null) {
            result = (T) new RawMeteredRecordImpl(
                    id,
                    meteringId,
                    recordedDate,
                    importedDate,
                    valueDate,
                    meteredTimestamp,
                    meteredDuration,
                    meteredValue,
                    tags
            );
        } else {
            result = (T) new RawTimedRecordImpl(
                    id,
                    meteringId,
                    recordedDate,
                    importedDate,
                    valueDate,
                    meteredTimestamp,
                    meteredDuration,
                    tags
            );
        }

        reset();
        return result;
    }

    private void defaults() {
        if (id == null) {
            id = UUID.randomUUID();
        }

        if (importedDate == null) {
            importedDate = OffsetDateTime.now(UTC);
        }

        if (recordedDate == null) {
            recordedDate = importedDate;
        }

        if (valueDate == null) {
            valueDate = recordedDate;
        }

        if (meteredTimestamp == null && meteredDuration != null) {
            meteredTimestamp = valueDate.minus(meteredDuration);
        }

        if (meteringId == null) {
            meteringId = id.toString();
        }
    }

    private void validate() {
    }

    private void reset() {
        this.id = null;
        this.meteringId = null;
        this.recordedDate = null;
        this.importedDate = null;
        this.valueDate = null;

        this.meteredValue = null;
        this.meteredTimestamp = null;
        this.meteredDuration = null;

        this.tags.clear();
    }


    public RawBillingRecordBuilder<T> setId(UUID id) {
        this.id = id;
        return this;
    }

    public RawBillingRecordBuilder<T> setValueDate(OffsetDateTime valueDate) {
        this.valueDate = valueDate;
        return this;
    }

    public RawBillingRecordBuilder<T> setRecordedDate(OffsetDateTime recordedDate) {
        this.recordedDate = recordedDate;
        return this;
    }

    public RawBillingRecordBuilder<T> setImportedDate(OffsetDateTime importedDate) {
        this.importedDate = importedDate;
        return this;
    }

    public RawBillingRecordBuilder<T> setMeteringId(String meteringId) {
        this.meteringId = meteringId;
        return this;
    }

    public RawBillingRecordBuilder<T> setMeteredTimestamp(OffsetDateTime meteredTimestamp) {
        this.meteredTimestamp = meteredTimestamp;
        return this;
    }

    public RawBillingRecordBuilder<T> setMeteredDuration(Duration meteredDuration) {
        this.meteredDuration = meteredDuration;
        return this;
    }

    public RawBillingRecordBuilder<T> setMeteredValue(BigDecimal meteredValue) {
        this.meteredValue = meteredValue;
        return this;
    }

    public RawBillingRecordBuilder<T> setTags(@NotNull final Map<String, String> tags) {
        this.tags.clear();

        if (tags != null) {
            this.tags.putAll(tags);
        }

        return this;
    }

    public RawBillingRecordBuilder<T> copy(final T orig) {
        this.id = orig.getId();
        this.meteringId = orig.getMeteringId();
        this.recordedDate = orig.getRecordedDate();
        this.importedDate = orig.getImportedDate();
        this.valueDate = orig.getValueDate();
        
        this.meteredTimestamp = orig.getMeteredTimestamp();
        this.meteredDuration = orig.getMeteredDuration();

        this.tags.clear();
        this.tags.putAll(orig.getTags());

        if (RawMeteredRecord.class.isAssignableFrom(orig.getClass())) {
            this.meteredValue = ((RawMeteredRecord) orig).getMeteredValue();
        }

        return this;
    }
}
