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

package de.kaiserpfalzedv.billing.api.base.impl;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import de.kaiserpfalzedv.billing.api.base.BaseTimedRecord;

/**
 * The base object for a billing record only defined by time.
 * 
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-09
 */
public abstract class AbstractTimedBillingRecordImpl extends AbstractBaseBillingRecordImpl
        implements BaseTimedRecord {
    private static final long serialVersionUID = 9007422427344034503L;


    public AbstractTimedBillingRecordImpl(
            final UUID id,
            final String meteringId,
            final OffsetDateTime recordedDate,
            final OffsetDateTime importedDate,
            final OffsetDateTime valueDate,
            final OffsetDateTime meteredStartDate,
            final Duration meteredDuration,
            final Map<String, String> tags
    ) {
        super(id, meteringId, recordedDate, importedDate, valueDate, meteredStartDate, meteredDuration, tags);
    }
}
