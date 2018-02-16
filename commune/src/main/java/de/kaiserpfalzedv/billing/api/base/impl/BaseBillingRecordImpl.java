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

import java.time.OffsetDateTime;
import java.util.UUID;

import de.kaiserpfalzedv.billing.api.base.BaseBillingRecord;
import de.kaiserpfalzedv.billing.api.common.impl.IdentifiableImpl;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-09
 */
public abstract class BaseBillingRecordImpl extends IdentifiableImpl implements BaseBillingRecord {
    private static final long serialVersionUID = 92480043612851920L;


    /**
     * Date this record will be valued at (this is the date determining the pricing within the tarif)
     */
    private final OffsetDateTime valueDate;

    /**
     * The date this record has been recorded at the metering unit. If no date is given defaults to
     * {@link #importedDate}
     */
    private final OffsetDateTime recordedDate;

    /**
     * The date and time this record has been imported into the billing system.
     */
    private final OffsetDateTime importedDate;

    /**
     * Id from the metering system for this record.
     */
    private final String meteringId;

    private String[] tagTitles;
    private String[] tags;


    protected BaseBillingRecordImpl(
            final UUID id,
            final String meteringId,
            final OffsetDateTime recordedDate,
            final OffsetDateTime importedDate,
            final OffsetDateTime valueDate
    ) {
        super(id);

        this.meteringId = meteringId;

        this.valueDate = valueDate;
        this.recordedDate = recordedDate;
        this.importedDate = importedDate;
    }

    @Override
    public String getMeteringId() {
        return meteringId;
    }

    @Override
    public OffsetDateTime getValueDate() {
        return valueDate;
    }

    @Override
    public OffsetDateTime getRecordedDate() {
        return recordedDate;
    }

    @Override
    public OffsetDateTime getImportedDate() {
        return importedDate;
    }

    @Override
    public String[] getTagTitles() {
        return tagTitles;
    }

    public void setTagTitles(final String[] tagTitles) {
        this.tagTitles = tagTitles;
    }

    @Override
    public String[] getTags() {
        return tags;
    }

    public void setTags(final String[] tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("valueDate", valueDate)
                .append("recordedDate", recordedDate)
                .append("importedDate", importedDate)
                .append("meteringId", meteringId)
                .toString();
    }
}