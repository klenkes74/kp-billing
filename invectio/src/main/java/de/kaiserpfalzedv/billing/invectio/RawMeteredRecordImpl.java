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

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import de.kaiserpfalzedv.billing.api.guided.CustomerGuide;
import de.kaiserpfalzedv.billing.api.guided.GuidedMeteredRecord;
import de.kaiserpfalzedv.billing.api.guided.GuidingBusinessExeption;
import de.kaiserpfalzedv.billing.api.guided.GuidingExecutor;
import de.kaiserpfalzedv.billing.api.guided.ProductGuide;
import de.kaiserpfalzedv.billing.api.imported.RawMeteredRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-09
 */
public class RawMeteredRecordImpl extends AbstractRawRecordImpl implements RawMeteredRecord {
    private static final long serialVersionUID = 8144772054887350133L;

    /**
     * The metered value.
     */
    private final BigDecimal meteredValue;

    protected RawMeteredRecordImpl(
            final UUID id,
            final String meteringId,
            final OffsetDateTime recordedDate,
            final OffsetDateTime importedDate,
            final OffsetDateTime valueDate,
            final OffsetDateTime meteredDate,
            final Duration meteredDuration,
            final BigDecimal meteredValue,
            final Map<String, String> tags
            ) {
        super(id, meteringId, recordedDate, importedDate, valueDate, meteredDate, meteredDuration, tags);

        this.meteredValue = meteredValue;
    }

    @Override
    public BigDecimal getMeteredValue() {
        return meteredValue;
    }


    @Override
    public GuidedMeteredRecord execute(
            final GuidingExecutor executor,
            final ProductGuide productGuide,
            final CustomerGuide customerGuide
    ) throws GuidingBusinessExeption {
        return executor.executeMeteredRecord(this, productGuide, customerGuide);
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("value", meteredValue)
                .toString();
    }
}
