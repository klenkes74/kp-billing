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

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

import de.kaiserpfalzedv.billing.api.guided.Customer;
import de.kaiserpfalzedv.billing.api.guided.GuidedTimedRecord;
import de.kaiserpfalzedv.billing.api.guided.ProductRecordInfo;
import de.kaiserpfalzedv.billing.api.rated.RatedTimedRecord;
import de.kaiserpfalzedv.billing.api.rated.RatingBusinessExeption;
import de.kaiserpfalzedv.billing.api.rated.RatingExecutor;
import de.kaiserpfalzedv.billing.api.rated.TarifingGuide;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-11
 */
public class GuidedTimedRecordImpl extends GuidedRecordImpl implements GuidedTimedRecord {
    private static final long serialVersionUID = 5634985591181920245L;

    GuidedTimedRecordImpl(
            final UUID id,
            final String meteringId,
            final Customer customer,
            final OffsetDateTime recordedDate,
            final OffsetDateTime importedDate,
            final OffsetDateTime valueDate,
            final ProductRecordInfo productInfo,
            final OffsetDateTime meteredStartTime,
            final Duration meteredDuration
    ) {
        super(id, meteringId, customer, recordedDate, importedDate, valueDate, productInfo, meteredStartTime, meteredDuration);
    }


    @Override
    public RatedTimedRecord rate(final RatingExecutor executor, final TarifingGuide tarifingGuide)
            throws RatingBusinessExeption {
        return executor.executeTimedRecord(this, tarifingGuide);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .toString();
    }
}
