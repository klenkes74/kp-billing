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

package de.kaiserpfalzedv.billing.api.rated.impl;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import javax.money.MonetaryAmount;

import de.kaiserpfalzedv.billing.api.guided.Customer;
import de.kaiserpfalzedv.billing.api.guided.ProductRecordInfo;
import de.kaiserpfalzedv.billing.api.guided.impl.GuidedMeteredRecordImpl;
import de.kaiserpfalzedv.billing.api.rated.RatedMeteredRecord;
import de.kaiserpfalzedv.billing.api.rated.Tarif;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class RatedMeteredRecordImpl extends GuidedMeteredRecordImpl implements RatedMeteredRecord {
    private static final long serialVersionUID = 956559841910152886L;

    private Tarif tarif;
    private MonetaryAmount amount;


    protected RatedMeteredRecordImpl(
            final UUID id,
            final String meteringId,
            final Customer customer,
            final OffsetDateTime recordedDate,
            final OffsetDateTime importedDate,
            final OffsetDateTime valueDate,
            final ProductRecordInfo productInfo,
            final BigDecimal meteredValue,
            final Tarif tarif,
            final MonetaryAmount amount
    ) {
        super(id, meteringId, customer, recordedDate, importedDate, valueDate, productInfo, meteredValue);

        this.tarif = tarif;
        this.amount = amount;
    }


    @Override
    public Tarif getTarif() {
        return tarif;
    }

    @Override
    public String getTarifName() {
        return tarif.getTarifName();
    }

    @Override
    public String getUnit() {
        return tarif.getUnit();
    }

    @Override
    public BigDecimal getUnitDivisor() {
        return tarif.getUnitDivisor();
    }

    @Override
    public MonetaryAmount getRate() {
        return tarif.getRate();
    }

    @Override
    public MonetaryAmount getAmount() {
        return amount;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("tarif", tarif)
                .append("amount", amount)
                .toString();
    }
}
