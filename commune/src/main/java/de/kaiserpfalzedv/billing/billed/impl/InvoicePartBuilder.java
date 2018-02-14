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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.billed.InvoiceItem;
import de.kaiserpfalzedv.billing.billed.InvoicePart;
import org.apache.commons.lang3.builder.Builder;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-12
 */
public class InvoicePartBuilder implements Builder<InvoicePart> {
    private static final Logger LOG = LoggerFactory.getLogger(InvoicePartBuilder.class);
    private final ArrayList<InvoiceItem> items = new ArrayList<>();
    private final InvoiceItemBuilder itemBuilder = new InvoiceItemBuilder();
    private UUID id;
    private String title;
    private MonetaryAmount amount;
    private CurrencyUnit currency;


    @Override
    public InvoicePart build() {
        validate();
        defaults();

        try {
            return new InvoicePartImpl(
                    id,
                    title,
                    amount,
                    items.toArray(new InvoiceItem[0])
            );
        } finally {
            reset();
        }
    }

    private void validate() {
        if (title == null) {
            throw new IllegalStateException("Can't create an invoice part without title!");
        }

        if (currency == null) {
            throw new IllegalStateException("Can't create an invoice part without setting the currency!");
        }
    }

    private void defaults() {
        if (id == null) {
            this.id = UUID.randomUUID();
        }

        if (amount == null) {
            calculateAmount();
        }
    }

    void reset() {
        id = null;
        title = null;
        items.clear();
        amount = null;
        currency = null;

        LOG.trace("{} reseted.", this);
    }

    private void calculateAmount() {
        amount = Money.of(0L, currency);

        for (InvoiceItem item : items) {
            amount = amount.add(item.getAmount());
        }
    }

    public InvoicePartBuilder setCurrency(final CurrencyUnit currency) {
        this.currency = currency;
        return this;
    }


    public InvoicePartBuilder setId(final UUID id) {
        this.id = id;
        return this;
    }

    public InvoicePartBuilder setTitle(final String title) {
        this.title = title;
        return this;
    }

    public InvoicePartBuilder setAmount(final MonetaryAmount amount) {
        this.amount = amount;
        return this;
    }

    public InvoicePartBuilder setItems(@NotNull final List<InvoiceItem> items) {
        this.items.clear();
        this.items.addAll(items);
        return this;
    }

    public InvoicePartBuilder clearItems() {
        items.clear();
        return this;
    }

    public InvoicePartBuilder addItem(
            final String title,
            final MonetaryAmount amount
    ) {
        items.add(
                itemBuilder
                        .setTitle(title)
                        .setAmount(amount)
                        .build()
        );
        return this;
    }

    public InvoicePartBuilder addItem(
            final UUID id,
            final String itemId,
            final String title,
            final MonetaryAmount amount
    ) {
        items.add(
                itemBuilder
                        .setId(id)
                        .setItemId(itemId)
                        .setTitle(title)
                        .setAmount(amount)
                        .build()
        );
        return this;
    }
}
