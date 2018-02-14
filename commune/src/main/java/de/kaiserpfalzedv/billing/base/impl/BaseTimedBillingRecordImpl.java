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

package de.kaiserpfalzedv.billing.base.impl;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.base.BaseTimedBillingRecord;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-09
 */
public abstract class BaseTimedBillingRecordImpl extends BaseBillingRecordImpl implements BaseTimedBillingRecord {
    private static final long serialVersionUID = 4056331340881073744L;


    /**
     * The start date of the billing event. May be the start of a call or the start of the hour of billed CPU usage.
     */
    private final OffsetDateTime meteredStartDate;

    /**
     * The duration of the billed event. May be the call duration or the period metered.
     */
    private final Duration meteredDuration;

    public BaseTimedBillingRecordImpl(
            final UUID id,
            final String meteringId,
            final OffsetDateTime recordedDate,
            final OffsetDateTime importedDate,
            final OffsetDateTime valueDate,
            final OffsetDateTime meteredStartDate,
            final Duration meteredDuration
    ) {
        super(id, meteringId, recordedDate, importedDate, valueDate);

        this.meteredStartDate = meteredStartDate;
        this.meteredDuration = meteredDuration;
    }

    public OffsetDateTime getMeteredStartDate() {
        return meteredStartDate;
    }

    public Duration getMeteredDuration() {
        return meteredDuration;
    }


    @Override
    public int compareTo(@NotNull BaseTimedBillingRecord o) {
        return getMeteredStartDate().compareTo(o.getMeteredStartDate());
    }

}
