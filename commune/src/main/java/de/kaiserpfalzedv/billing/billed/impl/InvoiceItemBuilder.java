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

package de.kaiserpfalzedv.billing.billed.impl;

import java.util.UUID;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

import de.kaiserpfalzedv.billing.billed.InvoiceItem;
import org.apache.commons.lang3.builder.Builder;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-12
 */
public class InvoiceItemBuilder implements Builder<InvoiceItem> {
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceItemBuilder.class);

    private UUID id;
    private String itemId;
    private String title;
    private MonetaryAmount amount;

    private CurrencyUnit currency;


    @Override
    public InvoiceItem build() {
        validate();
        defaults();

        try {
            return new InvoiceItemImpl(
                    id,
                    itemId,
                    title,
                    amount
            );
        } finally {
            reset();
        }
    }

    private void validate() {
        if (currency == null && amount == null) {
            throw new IllegalStateException("Can't create an invoice item without setting the currency or amount!");
        }

        if (title == null) {
            throw new IllegalStateException("Can't create an invoice item without title!");
        }
    }

    private void defaults() {
        if (id == null) {
            id = UUID.randomUUID();
        }

        if (itemId == null) {
            itemId = id.toString();
        }

        if (amount == null) {
            amount = Money.of(0L, currency);
        }
    }

    public void reset() {
        id = null;
        itemId = null;
        title = null;
        amount = null;
        currency = null;

        LOG.trace("{} reseted.", this);
    }


    public InvoiceItemBuilder setCurrency(final CurrencyUnit currency) {
        this.currency = currency;
        return this;
    }


    public InvoiceItemBuilder setId(final UUID id) {
        this.id = id;
        return this;
    }

    public InvoiceItemBuilder setItemId(final String itemId) {
        this.itemId = itemId;
        return this;
    }

    public InvoiceItemBuilder setTitle(final String title) {
        this.title = title;
        return this;
    }

    public InvoiceItemBuilder setAmount(final MonetaryAmount amount) {
        this.amount = amount;
        return this;
    }
}
