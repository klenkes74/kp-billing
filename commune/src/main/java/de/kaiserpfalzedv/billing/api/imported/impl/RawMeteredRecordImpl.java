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

package de.kaiserpfalzedv.billing.api.imported.impl;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import de.kaiserpfalzedv.billing.api.base.impl.BaseMeteredBillingRecordImpl;
import de.kaiserpfalzedv.billing.api.guided.GuidedMeteredRecord;
import de.kaiserpfalzedv.billing.api.guided.GuidingBusinessExeption;
import de.kaiserpfalzedv.billing.api.guided.GuidingExecutor;
import de.kaiserpfalzedv.billing.api.imported.RawMeteredRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-09
 */
public class RawMeteredRecordImpl extends BaseMeteredBillingRecordImpl implements RawMeteredRecord {
    private static final long serialVersionUID = 8040151738538059916L;


    /**
     * The metering product from the metering system
     */
    private final String meteringProduct;

    /**
     * The customer information from the metering system
     */
    private final String meteredCustomer;

    protected RawMeteredRecordImpl(
            final UUID id,
            final String meteringId,
            final OffsetDateTime recordedDate,
            final OffsetDateTime importedDate,
            final OffsetDateTime valueDate,
            final String meteringProduct,
            final String meteredCustomer,
            final BigDecimal meteredValue
    ) {
        super(id, meteringId, recordedDate, importedDate, valueDate, meteredValue);

        this.meteringProduct = meteringProduct;
        this.meteredCustomer = meteredCustomer;
    }

    @Override
    public String getMeteredCustomer() {
        return meteredCustomer;
    }

    @Override
    public String getMeteringProduct() {
        return meteringProduct;
    }


    @Override
    public GuidedMeteredRecord execute(GuidingExecutor executor) throws GuidingBusinessExeption {
        return executor.execute(this);
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
