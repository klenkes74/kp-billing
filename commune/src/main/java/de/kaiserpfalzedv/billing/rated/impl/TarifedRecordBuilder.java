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

package de.kaiserpfalzedv.billing.rated.impl;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

import javax.money.MonetaryAmount;

import de.kaiserpfalzedv.billing.guided.Customer;
import de.kaiserpfalzedv.billing.guided.ProductRecordInfo;
import de.kaiserpfalzedv.billing.rated.Tarif;
import de.kaiserpfalzedv.billing.rated.TarifedBaseRecord;
import de.kaiserpfalzedv.billing.rated.TarifedMeteredRecord;
import de.kaiserpfalzedv.billing.rated.TarifedTimedRecord;
import org.apache.commons.lang3.builder.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.math.RoundingMode.HALF_UP;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class TarifedRecordBuilder<T extends TarifedBaseRecord> implements Builder<T> {
    private static final Logger LOG = LoggerFactory.getLogger(TarifedRecordBuilder.class);

    private static final ZoneId UTC = ZoneId.of("UTC");

    /**
     * precision of the rate calculations. We default to 5 decimals
     */
    private static final int PRECISION = 5;


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

    private Tarif tarif;

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
                MonetaryAmount amount = tarif.getRate()
                                             .multiply(
                                                     meteredValue.divide(tarif.getUnitDivisor(), PRECISION, HALF_UP)
                                             );

                LOG.debug("Calculating metered value / tarif unit divisor * rate: {} / {} * {} = {}", meteredValue, tarif
                        .getUnitDivisor(), tarif.getRate(), amount);

                return (T) new TarifedMeteredRecordImpl(
                        id,
                        meteringId,
                        customer,
                        recordedDate,
                        importedDate,
                        valueDate,
                        productInfo,
                        meteredValue,
                        tarif,
                        amount
                );
            } else {
                MonetaryAmount amount = tarif.getRate()
                                             .multiply(
                                                     BigDecimal
                                                             .valueOf(meteredDuration.getSeconds())
                                                             .divide(tarif.getUnitDivisor(), PRECISION, HALF_UP)
                                             );

                LOG.debug("Calculating duration (in s) / tarif unit divisor * rate: {} / {} * {} = {}", meteredDuration.getSeconds(), tarif
                        .getUnitDivisor(), tarif.getRate(), amount);

                return (T) new TarifedTimedRecordImpl(
                        id,
                        meteringId,
                        customer,
                        recordedDate,
                        importedDate,
                        valueDate,
                        productInfo,
                        meteredStartDate,
                        meteredDuration,
                        tarif,
                        amount

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
            throw new IllegalStateException("Can't create a raw record without a product info");
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


    public TarifedRecordBuilder<T> setId(final UUID id) {
        this.id = id;
        return this;
    }

    public TarifedRecordBuilder<T> setValueDate(final OffsetDateTime valueDate) {
        this.valueDate = valueDate;
        return this;
    }

    public TarifedRecordBuilder<T> setRecordedDate(final OffsetDateTime recordedDate) {
        this.recordedDate = recordedDate;
        return this;
    }

    public TarifedRecordBuilder<T> setImportedDate(final OffsetDateTime importedDate) {
        this.importedDate = importedDate;
        return this;
    }

    public TarifedRecordBuilder<T> setMeteringId(final String meteringId) {
        this.meteringId = meteringId;
        return this;
    }

    public TarifedRecordBuilder<T> setTarif(final Tarif tarif) {
        this.tarif = tarif;
        return this;
    }

    public TarifedRecordBuilder<T> setCustomer(final Customer customer) {
        this.customer = customer;
        return this;
    }

    public TarifedRecordBuilder<T> setProductInfo(final ProductRecordInfo productInfo) {
        this.productInfo = productInfo;
        return this;
    }

    public TarifedRecordBuilder<T> setMeteredValue(final BigDecimal meteredValue) {
        this.meteredValue = meteredValue;
        return this;
    }

    public TarifedRecordBuilder<T> setMeteredStartDate(final OffsetDateTime meteredStartDate) {
        this.meteredStartDate = meteredStartDate;
        return this;
    }

    public TarifedRecordBuilder<T> setMeteredDuration(final Duration meteredDuration) {
        this.meteredDuration = meteredDuration;
        return this;
    }

    public TarifedRecordBuilder<T> copy(final T orig) {
        this.id = orig.getId();
        this.meteringId = orig.getMeteringId();
        this.recordedDate = orig.getRecordedDate();
        this.importedDate = orig.getImportedDate();
        this.valueDate = orig.getValueDate();
        this.productInfo = orig.getProductInfo();
        this.customer = orig.getCustomer();
        this.tarif = orig.getTarif();

        if (TarifedMeteredRecord.class.isAssignableFrom(orig.getClass())) {
            this.meteredValue = ((TarifedMeteredRecord) orig).getMeteredValue();
        } else {
            this.meteredStartDate = ((TarifedTimedRecord) orig).getMeteredStartDate();
            this.meteredDuration = ((TarifedTimedRecord) orig).getMeteredDuration();
        }

        return this;
    }
}
