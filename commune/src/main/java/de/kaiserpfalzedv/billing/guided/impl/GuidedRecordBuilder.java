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

package de.kaiserpfalzedv.billing.guided.impl;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

import de.kaiserpfalzedv.billing.guided.Customer;
import de.kaiserpfalzedv.billing.guided.GuidedBaseRecord;
import de.kaiserpfalzedv.billing.guided.GuidedMeteredRecord;
import de.kaiserpfalzedv.billing.guided.GuidedTimedRecord;
import de.kaiserpfalzedv.billing.guided.ProductRecordInfo;
import org.apache.commons.lang3.builder.Builder;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class GuidedRecordBuilder<T extends GuidedBaseRecord> implements Builder<T> {
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
    private ProductRecordInfo productInfo;

    /**
     * The customer for this record.
     */
    private Customer customer;


    @SuppressWarnings("unchecked")
    @Override
    public T build() {
        defaults();
        validate();

        try {
            if (meteredValue != null) {
                return (T) new GuidedMeteredRecordImpl(
                        id,
                        meteringId,
                        customer,
                        recordedDate,
                        importedDate,
                        valueDate,
                        productInfo,
                        meteredValue
                );
            } else {
                return (T) new GuidedTimedRecordImpl(
                        id,
                        meteringId,
                        customer,
                        recordedDate,
                        importedDate,
                        valueDate,
                        productInfo,
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
        if (productInfo == null) {
            throw new IllegalStateException("Can't create a raw record without a product hint");
        }

        if (customer == null) {
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
        this.productInfo = null;
        this.customer = null;

        this.meteredValue = null;
        this.meteredStartDate = null;
        this.meteredDuration = null;
    }


    public GuidedRecordBuilder<T> setId(UUID id) {
        this.id = id;
        return this;
    }

    public GuidedRecordBuilder<T> setValueDate(OffsetDateTime valueDate) {
        this.valueDate = valueDate;
        return this;
    }

    public GuidedRecordBuilder<T> setRecordedDate(OffsetDateTime recordedDate) {
        this.recordedDate = recordedDate;
        return this;
    }

    public GuidedRecordBuilder<T> setImportedDate(OffsetDateTime importedDate) {
        this.importedDate = importedDate;
        return this;
    }

    public GuidedRecordBuilder<T> setMeteringId(String meteringId) {
        this.meteringId = meteringId;
        return this;
    }

    public GuidedRecordBuilder<T> setMeteredValue(BigDecimal meteredValue) {
        this.meteredValue = meteredValue;
        return this;
    }

    public GuidedRecordBuilder<T> setMeteredStartDate(OffsetDateTime meteredStartDate) {
        this.meteredStartDate = meteredStartDate;
        return this;
    }

    public GuidedRecordBuilder<T> setMeteredDuration(Duration meteredDuration) {
        this.meteredDuration = meteredDuration;
        return this;
    }

    public GuidedRecordBuilder<T> setProductInfo(ProductRecordInfo productInfo) {
        this.productInfo = productInfo;
        return this;
    }

    public GuidedRecordBuilder<T> setCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public GuidedRecordBuilder<T> copy(final T orig) {
        this.id = orig.getId();
        this.meteringId = orig.getMeteringId();
        this.recordedDate = orig.getRecordedDate();
        this.importedDate = orig.getImportedDate();
        this.valueDate = orig.getValueDate();
        this.productInfo = orig.getProductInfo();
        this.customer = orig.getCustomer();

        if (GuidedMeteredRecord.class.isAssignableFrom(orig.getClass())) {
            this.meteredValue = ((GuidedMeteredRecord) orig).getMeteredValue();
        } else {
            this.meteredStartDate = ((GuidedTimedRecord) orig).getMeteredStartDate();
            this.meteredDuration = ((GuidedTimedRecord) orig).getMeteredDuration();
        }

        return this;
    }
}
