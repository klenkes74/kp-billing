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
import java.util.UUID;

import de.kaiserpfalzedv.billing.api.base.impl.BaseTimedBillingRecordImpl;
import de.kaiserpfalzedv.billing.api.imported.RawBaseRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-09
 */
public abstract class RawRecordImpl extends BaseTimedBillingRecordImpl implements RawBaseRecord {
    private static final long serialVersionUID = 7396673847352746427L;

    /**
     * The metering product from the metering system
     */
    private String meteringProduct;

    /**
     * The customer for this record.
     */
    private String meteredCustomer;

    RawRecordImpl(
            final UUID id,
            final String meteringId,
            final OffsetDateTime recordedDate,
            final OffsetDateTime importedDate,
            final OffsetDateTime valueDate,
            final OffsetDateTime meteredStartDate,
            final Duration meteredDuration
    ) {
        super(id, meteringId, recordedDate, importedDate, valueDate, meteredStartDate, meteredDuration);
    }

    @Override
    public String getMeteredCustomer() {
        return meteredCustomer;
    }

    public void setMeteredCustomer(final String meteredCustomer) {
        this.meteredCustomer = meteredCustomer;
    }

    @Override
    public String getMeteringProduct() {
        return meteringProduct;
    }

    public void setMeteringProduct(final String meteringProduct) {
        this.meteringProduct = meteringProduct;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("meteringProduct", meteringProduct)
                .append("meteredCustomer", meteredCustomer)
                .toString();
    }
}
