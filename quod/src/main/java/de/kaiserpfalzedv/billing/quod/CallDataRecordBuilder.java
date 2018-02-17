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

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.UUID;

import javax.money.MonetaryAmount;
import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.api.cdr.CallDataRecord;
import de.kaiserpfalzedv.billing.api.common.BuilderException;
import de.kaiserpfalzedv.billing.api.common.CurrencyProvider;
import de.kaiserpfalzedv.billing.api.common.impl.DefaultCurrencyProvider;
import org.apache.commons.lang3.builder.Builder;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-17
 */
public class CallDataRecordBuilder implements Builder<CallDataRecord> {
    private static final Logger LOG = LoggerFactory.getLogger(CallDataRecordBuilder.class);

    private UUID id;
    private String description;
    private String tarifName;
    private BigDecimal tarifUnitDivisor;
    private String tarifUnit;
    private MonetaryAmount tarifRate;
    private MonetaryAmount amount;
    private OffsetDateTime meteredTimestamp;
    private Duration meteredDuration;
    private BigDecimal meteredValue;

    private CurrencyProvider currencyProvider;

    /**
     * @return the call data record
     * @throws BuilderException If the data is not sufficient to build the {@link CallDataRecord}.
     */
    @Override
    public CallDataRecord build() {
        validate();
        defaults();

        CallDataRecord result = new CallDataRecordImpl(
                id,
                description,

                tarifName,
                tarifRate,
                tarifUnit,
                tarifUnitDivisor,

                meteredTimestamp,
                meteredDuration,
                meteredValue,

                amount
        );

        LOG.trace("Created CDR: {}", result);
        reset();
        return result;
    }

    private void validate() {
        HashSet<String> failures = new HashSet<>();

        if (isBlank(description)) {
            failures.add("No description given");
        }

        if (isBlank(tarifName)) {
            failures.add("No tarif name given");
        }

        if (tarifRate == null) {
            failures.add("No tarif rate given");
        }

        if (isBlank(tarifUnit)) {
            failures.add("No tarif unit given");
        }

        if (meteredTimestamp == null) {
            failures.add("No timestamp for the cdr given");
        }

        if (! failures.isEmpty()) {
            throw new BuilderException(CallDataRecord.class, failures);
        }
    }

    private void defaults() {
        if (id == null) {
            id = UUID.randomUUID();
        }

        if (tarifUnitDivisor == null) {
            tarifUnitDivisor = BigDecimal.ONE;
        }

        if (meteredDuration == null) {
            meteredDuration = Duration.ZERO;
        }

        if (meteredValue == null) {
            meteredValue = BigDecimal.ZERO;
        }


        if (amount == null) {
            if (currencyProvider == null) {
                currencyProvider = new DefaultCurrencyProvider();
            }
            
            amount = Money.of(0L, currencyProvider.getCurrency());
        }
    }

    public void reset() {
        id = null;
        description = null;
        tarifName = null;
        tarifRate = null;
        tarifUnitDivisor = null;
        tarifUnit = null;
        meteredTimestamp = null;
        meteredDuration = null;
        meteredValue = null;
        amount = null;
        currencyProvider = null;

        LOG.debug("CDR Builder reset: {}", this);
    }


    public CallDataRecordBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public CallDataRecordBuilder withDescription(String description) {
        this.description = description;
        return this;
    }
    

    public CallDataRecordBuilder withTarifName(String tarifName) {
        this.tarifName = tarifName;
        return this;
    }

    public CallDataRecordBuilder withTarifRate(MonetaryAmount tarifRate) {
        this.tarifRate = tarifRate;
        return this;
    }

    public CallDataRecordBuilder withTarifUnit(String unit) {
        this.tarifUnit = unit;
        return this;
    }

    public CallDataRecordBuilder withTarifUnitDivisor(BigDecimal unitDivisor) {
        this.tarifUnitDivisor = unitDivisor;
        return this;
    }


    public CallDataRecordBuilder withMeteredTimestamp(OffsetDateTime meteredTimestamp) {
        this.meteredTimestamp = meteredTimestamp;
        return this;
    }

    public CallDataRecordBuilder withMeteredDuration(Duration meteredDuration) {
        this.meteredDuration = meteredDuration;
        return this;
    }

    public CallDataRecordBuilder withMeteredValue(BigDecimal meteredValue) {
        this.meteredValue = meteredValue;
        return this;
    }


    public CallDataRecordBuilder withAmount(MonetaryAmount amount) {
        this.amount = amount;
        return this;
    }


    public CallDataRecordBuilder withCurrencyProvider(@NotNull final CurrencyProvider provider) {
        this.currencyProvider = provider;
        return this;
    }
}