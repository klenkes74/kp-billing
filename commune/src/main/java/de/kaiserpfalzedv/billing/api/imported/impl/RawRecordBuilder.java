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

package de.kaiserpfalzedv.billing.api.imported.impl;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

import de.kaiserpfalzedv.billing.api.imported.RawBaseRecord;
import de.kaiserpfalzedv.billing.api.imported.RawMeteredRecord;
import de.kaiserpfalzedv.billing.api.imported.RawTimedRecord;
import org.apache.commons.lang3.builder.Builder;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class RawRecordBuilder<T extends RawBaseRecord> implements Builder<T> {
    private static final ZoneId UTC = ZoneId.of("UTC");


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
    private OffsetDateTime meteredStartDate;

    /**
     * The duration of the billed event. May be the call duration or the period metered.
     */
    private Duration meteredDuration;

    /**
     * The metering product from the metering system
     */
    private String meteringProduct;

    /**
     * The customer for this record.
     */
    private String meteredCustomer;


    @SuppressWarnings("unchecked")
    @Override
    public T build() {
        defaults();
        validate();

        try {
            if (meteredValue != null) {
                return (T) new RawMeteredRecordImpl(
                        id,
                        meteringId,
                        recordedDate,
                        importedDate,
                        valueDate,
                        meteringProduct,
                        meteredCustomer,
                        meteredValue
                );
            } else {
                return (T) new RawTimedRecordImpl(
                        id,
                        meteringId,
                        recordedDate,
                        importedDate,
                        valueDate,
                        meteringProduct,
                        meteredCustomer,
                        meteredStartDate,
                        meteredDuration
                );
            }
        } finally {
            reset();
        }
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

        if (meteredStartDate == null && meteredDuration != null) {
            meteredStartDate = valueDate.minus(meteredDuration);
        }

        if (meteringId == null) {
            meteringId = id.toString();
        }
    }

    private void validate() {
        if (isBlank(meteringProduct)) {
            throw new IllegalStateException("Can't create a raw record without a product hint");
        }

        if (isBlank(meteredCustomer)) {
            throw new IllegalStateException("Can't create a raw record without a customer");
        }

        if (meteredValue != null && meteredDuration != null) {
            throw new IllegalStateException("Can't decide if its a raw metered record or a raw timed record. Please only use value or duration!");
        }

        if (meteredValue == null && meteredDuration == null) {
            throw new IllegalStateException("You have to give either a duration or a metered value for a valid raw record!");
        }
    }

    private void reset() {
        this.id = null;
        this.meteringId = null;
        this.recordedDate = null;
        this.importedDate = null;
        this.valueDate = null;
        this.meteringProduct = null;
        this.meteredCustomer = null;

        this.meteredValue = null;
        this.meteredStartDate = null;
        this.meteredDuration = null;
    }


    public RawRecordBuilder<T> setId(UUID id) {
        this.id = id;
        return this;
    }

    public RawRecordBuilder<T> setValueDate(OffsetDateTime valueDate) {
        this.valueDate = valueDate;
        return this;
    }

    public RawRecordBuilder<T> setRecordedDate(OffsetDateTime recordedDate) {
        this.recordedDate = recordedDate;
        return this;
    }

    public RawRecordBuilder<T> setImportedDate(OffsetDateTime importedDate) {
        this.importedDate = importedDate;
        return this;
    }

    public RawRecordBuilder<T> setMeteringId(String meteringId) {
        this.meteringId = meteringId;
        return this;
    }

    public RawRecordBuilder<T> setMeteredValue(BigDecimal meteredValue) {
        this.meteredValue = meteredValue;
        return this;
    }

    public RawRecordBuilder<T> setMeteredStartDate(OffsetDateTime meteredStartDate) {
        this.meteredStartDate = meteredStartDate;
        return this;
    }

    public RawRecordBuilder<T> setMeteredDuration(Duration meteredDuration) {
        this.meteredDuration = meteredDuration;
        return this;
    }

    public RawRecordBuilder<T> setMeteringProduct(String meteringProduct) {
        this.meteringProduct = meteringProduct;
        return this;
    }

    public RawRecordBuilder<T> setMeteredCustomer(String meteredCustomer) {
        this.meteredCustomer = meteredCustomer;
        return this;
    }

    public RawRecordBuilder<T> copy(final T orig) {
        this.id = orig.getId();
        this.meteringId = orig.getMeteringId();
        this.recordedDate = orig.getRecordedDate();
        this.importedDate = orig.getImportedDate();
        this.valueDate = orig.getValueDate();
        this.meteringProduct = orig.getMeteringProduct();
        this.meteredCustomer = orig.getMeteredCustomer();

        if (RawMeteredRecord.class.isAssignableFrom(orig.getClass())) {
            this.meteredValue = ((RawMeteredRecord) orig).getMeteredValue();
        } else {
            this.meteredStartDate = ((RawTimedRecord) orig).getMeteredStartDate();
            this.meteredDuration = ((RawTimedRecord) orig).getMeteredDuration();
        }

        return this;
    }
}
