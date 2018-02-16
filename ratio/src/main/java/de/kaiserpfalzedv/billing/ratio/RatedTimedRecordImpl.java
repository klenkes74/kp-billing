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

package de.kaiserpfalzedv.billing.ratio;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

import javax.money.MonetaryAmount;

import de.kaiserpfalzedv.billing.api.guided.Customer;
import de.kaiserpfalzedv.billing.api.guided.ProductRecordInfo;
import de.kaiserpfalzedv.billing.api.rated.RatedTimedRecord;
import de.kaiserpfalzedv.billing.api.rated.Tarif;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class RatedTimedRecordImpl extends RatedRecordImpl implements RatedTimedRecord {
    private static final long serialVersionUID = 4274710153081800419L;

    RatedTimedRecordImpl(
            final UUID id,
            final String meteringId,
            final Customer customer,
            final OffsetDateTime recordedDate,
            final OffsetDateTime importedDate,
            final OffsetDateTime valueDate,
            final ProductRecordInfo productInfo,
            final OffsetDateTime meteredStartDate,
            final Duration meteredDuration,
            final Tarif tarif,
            final MonetaryAmount amount
    ) {
        super(id, meteringId, customer, recordedDate, importedDate, valueDate, productInfo,
              meteredStartDate, meteredDuration, tarif, amount);
    }
}
