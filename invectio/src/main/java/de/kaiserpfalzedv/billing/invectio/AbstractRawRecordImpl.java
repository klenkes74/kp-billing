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

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import de.kaiserpfalzedv.billing.api.base.impl.AbstractBaseBillingRecordImpl;
import de.kaiserpfalzedv.billing.api.imported.RawBaseRecord;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-09
 */
public abstract class AbstractRawRecordImpl extends AbstractBaseBillingRecordImpl implements RawBaseRecord {
    private static final long serialVersionUID = 9084979105229965327L;

    AbstractRawRecordImpl(
            final UUID id,
            final String meteringId,
            final OffsetDateTime recordedDate,
            final OffsetDateTime importedDate,
            final OffsetDateTime valueDate,
            final OffsetDateTime meteredTimestamp,
            final Duration meteredDuration,
            final Map<String, String> tags
            ) {
        super(id, meteringId, recordedDate, importedDate, valueDate, meteredTimestamp, meteredDuration, tags);
    }
}
