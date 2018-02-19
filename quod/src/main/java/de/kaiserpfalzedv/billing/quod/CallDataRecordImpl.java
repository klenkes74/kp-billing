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
import java.util.UUID;

import javax.money.MonetaryAmount;
import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.api.cdr.CallDataRecord;
import de.kaiserpfalzedv.billing.api.common.impl.IdentifiableImpl;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-17
 */
public class CallDataRecordImpl extends IdentifiableImpl implements CallDataRecord {
    private final String description;
    private final String tarifName;
    private final MonetaryAmount tarifRate;
    private final String tarifUnit;
    private final BigDecimal tarifUnitDivisor;
    private final OffsetDateTime meteredTimestamp;
    private final Duration meteredDuration;
    private final BigDecimal meteredValue;
    private final MonetaryAmount amount;

    protected CallDataRecordImpl(
            @NotNull final UUID id,
            @NotNull final String description,
            @NotNull final String tarifName,
            @NotNull final MonetaryAmount tarifRate,
            @NotNull final String tarifUnit,
            @NotNull final BigDecimal tarifUnitDivisor,
            @NotNull final OffsetDateTime meteredTimestamp,
            @NotNull final Duration meteredDuration,
            @NotNull final BigDecimal meteredValue,
            @NotNull final MonetaryAmount amount
    ) {
        super(id);

        this.description = description;
        this.tarifName = tarifName;
        this.tarifUnitDivisor = tarifUnitDivisor;
        this.tarifUnit = tarifUnit;
        this.tarifRate = tarifRate;
        this.amount = amount;
        this.meteredTimestamp = meteredTimestamp;
        this.meteredDuration = meteredDuration;
        this.meteredValue = meteredValue;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getTarifName() {
        return tarifName;
    }

    @Override
    public MonetaryAmount getTarifRate() {
        return tarifRate;
    }

    @Override
    public String getTarifUnit() {
        return tarifUnit;
    }

    @Override
    public BigDecimal getTarifUnitDivisor() {
        return tarifUnitDivisor;
    }

    @Override
    public OffsetDateTime getMeteredTimestamp() {
        return meteredTimestamp;
    }

    @Override
    public Duration getMeteredDuration() {
        return meteredDuration;
    }

    @Override
    public BigDecimal getMeteredValue() {
        return meteredValue;
    }

    @Override
    public MonetaryAmount getAmount() {
        return amount;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("description", description)
                .append("tarifName", tarifName)
                .append("tarifRate", tarifRate)
                .append("tarifUnitDivisor", tarifUnitDivisor)
                .append("tarifUnit", tarifUnit)
                .append("meteredTimestamp", meteredTimestamp)
                .append("meteredDuration", meteredDuration)
                .append("meteredValue", meteredValue)
                .append("amount", amount)
                .toString();
    }
}