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

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.api.guided.Customer;
import de.kaiserpfalzedv.billing.api.guided.GuidedBaseRecord;
import de.kaiserpfalzedv.billing.api.guided.GuidedMeteredRecord;
import de.kaiserpfalzedv.billing.api.guided.ProductRecordInfo;
import org.apache.commons.lang3.builder.Builder;

import static java.time.ZoneOffset.UTC;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class GuidedRecordBuilder<T extends GuidedBaseRecord> implements Builder<T> {
    
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

    /**
     * The metering product from the metering system
     */
    private ProductRecordInfo productInfo;

    /**
     * The customer for this record.
     */
    private Customer customer;

    private final HashMap<String, String> tags = new HashMap<>();


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
                        meteredTimestamp,
                        meteredDuration,
                        meteredValue,
                        tags
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
                        meteredTimestamp,
                        meteredDuration,
                        tags
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

        if (meteredTimestamp == null && meteredDuration != null) {
            meteredTimestamp = valueDate.minus(meteredDuration);
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
        this.meteredTimestamp = null;
        this.meteredDuration = null;
    }


    public GuidedRecordBuilder<T> setId(final UUID id) {
        this.id = id;
        return this;
    }

    public GuidedRecordBuilder<T> setValueDate(final OffsetDateTime valueDate) {
        this.valueDate = valueDate;
        return this;
    }

    public GuidedRecordBuilder<T> setRecordedDate(final OffsetDateTime recordedDate) {
        this.recordedDate = recordedDate;
        return this;
    }

    public GuidedRecordBuilder<T> setImportedDate(final OffsetDateTime importedDate) {
        this.importedDate = importedDate;
        return this;
    }

    public GuidedRecordBuilder<T> setMeteringId(final String meteringId) {
        this.meteringId = meteringId;
        return this;
    }

    public GuidedRecordBuilder<T> setMeteredValue(final BigDecimal meteredValue) {
        this.meteredValue = meteredValue;
        return this;
    }

    public GuidedRecordBuilder<T> setMeteredTimestamp(final OffsetDateTime meteredTimestamp) {
        this.meteredTimestamp = meteredTimestamp;
        return this;
    }

    public GuidedRecordBuilder<T> setMeteredDuration(final Duration meteredDuration) {
        this.meteredDuration = meteredDuration;
        return this;
    }

    public GuidedRecordBuilder<T> setProductInfo(final ProductRecordInfo productInfo) {
        this.productInfo = productInfo;
        return this;
    }

    public GuidedRecordBuilder<T> setCustomer(final Customer customer) {
        this.customer = customer;
        return this;
    }

    public GuidedRecordBuilder<T> setTags(@NotNull final Map<String, String> tags) {
        this.tags.clear();

        if (tags != null) {
            this.tags.putAll(tags);
        }

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

        this.tags.clear();
        this.tags.putAll(orig.getTags());

        this.meteredTimestamp = orig.getMeteredTimestamp();
        this.meteredDuration = orig.getMeteredDuration();

        if (GuidedMeteredRecord.class.isAssignableFrom(orig.getClass())) {
            this.meteredValue = ((GuidedMeteredRecord) orig).getMeteredValue();
        }

        return this;
    }
}
