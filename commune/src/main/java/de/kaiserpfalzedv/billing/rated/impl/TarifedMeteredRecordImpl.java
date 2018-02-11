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

package de.kaiserpfalzedv.billing.rated.impl;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import javax.money.MonetaryAmount;

import de.kaiserpfalzedv.billing.imported.impl.RawMeteredRecordImpl;
import de.kaiserpfalzedv.billing.rated.TarifedMeteredRecord;
import de.kaiserpfalzedv.billing.tarif.Tarif;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class TarifedMeteredRecordImpl extends RawMeteredRecordImpl implements TarifedMeteredRecord {
    private static final long serialVersionUID = -1517864163494689498L;


    private String meteredCustomer;
    private String meteringProduct;
    private Tarif tarif;
    private MonetaryAmount amount;


    protected TarifedMeteredRecordImpl(
            final UUID id,
            final String meteringId,
            final OffsetDateTime recordedDate,
            final OffsetDateTime importedDate,
            final OffsetDateTime valueDate,
            final String meteredCustomer,
            final String meteringProduct,
            final BigDecimal meteredValue,
            final Tarif tarif,
            final MonetaryAmount amount
    ) {
        super(id, meteringId, recordedDate, importedDate, valueDate, meteringProduct, meteredCustomer, meteredValue);

        this.meteredCustomer = meteredCustomer;
        this.meteringProduct = meteringProduct;
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
    public MonetaryAmount getRate() {
        return tarif.getRate();
    }

    @Override
    public MonetaryAmount getAmount() {
        return amount;
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
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("tarif", tarif)
                .append("customer", meteredCustomer)
                .append("product", meteringProduct)
                .append("amount", amount)
                .toString();
    }
}
