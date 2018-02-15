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

package de.kaiserpfalzedv.billing.api.billed.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

import de.kaiserpfalzedv.billing.api.base.impl.DefaultCurrencyProvider;
import de.kaiserpfalzedv.billing.api.billed.Attachment;
import de.kaiserpfalzedv.billing.api.billed.Invoice;
import de.kaiserpfalzedv.billing.api.billed.InvoiceItem;
import de.kaiserpfalzedv.billing.api.billed.InvoicePart;
import de.kaiserpfalzedv.billing.api.guided.Customer;
import org.apache.commons.lang3.builder.Builder;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-12
 */
public class InvoiceBuilder implements Builder<Invoice> {
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceBuilder.class);

    private static final ZoneId UTC = ZoneId.of("UTC");
    private final ArrayList<InvoicePart> parts = new ArrayList<>();
    private final ArrayList<Attachment> attachments = new ArrayList<>();
    private final InvoicePartBuilder partBuilder = new InvoicePartBuilder();
    private UUID id;
    private String invoiceNumber;
    private LocalDate invoiceDate;
    private Customer customer;
    private MonetaryAmount amount;
    private CurrencyUnit currency;

    @Override
    public Invoice build() {
        validate();
        defaults();

        try {
            return new InvoiceImpl(
                    id,
                    invoiceNumber,
                    invoiceDate,
                    customer,
                    amount,
                    parts,
                    attachments
            );
        } finally {
            reset();
        }
    }

    private void validate() {
        if (customer == null) {
            throw new IllegalStateException("Can't create an invoice without customer!");
        }

        if (parts.isEmpty() && LOG.isInfoEnabled()) {
            LOG.info("{} is creating an empty invoice{}", this, (id != null ? ": " + id.toString() : ""));
        }
    }

    private void defaults() {
        if (id == null) {
            id = UUID.randomUUID();
        }

        if (invoiceNumber == null) {
            invoiceNumber = id.toString();
        }

        if (invoiceDate == null) {
            invoiceDate = LocalDate.now(UTC);
        }

        if (currency == null) {
            currency = new DefaultCurrencyProvider().getCurrency();
        }

        if (amount == null) {
            calculateAmount();
        }
    }

    public void reset() {
        LOG.trace("{} is being reset ...", this);

        id = null;
        customer = null;
        invoiceDate = null;
        invoiceNumber = null;
        amount = null;
        currency = null;
        parts.clear();
        attachments.clear();
        partBuilder.reset();
    }

    private void calculateAmount() {
        amount = Money.of(0L, currency);

        for (InvoicePart part : parts) {
            amount.add(part.getAmount());
        }
    }

    public InvoiceBuilder setId(final UUID id) {
        this.id = id;
        return this;
    }

    public InvoiceBuilder setCustomer(final Customer customer) {
        this.customer = customer;
        return this;
    }

    public InvoiceBuilder setInvoiceDate(final LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public InvoiceBuilder setInvoiceNumber(final String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
        return this;
    }

    public InvoiceBuilder setCurrency(final CurrencyUnit currency) {
        this.currency = currency;
        return this;
    }

    public InvoiceBuilder setAmount(final MonetaryAmount amount) {
        this.amount = amount;
        return this;
    }

    public InvoiceBuilder addPart(
            final String title,
            final List<InvoiceItem> items
    ) {
        if (currency == null) {
            throw new IllegalStateException(
                    "Can't add a part without currency information. "
                            + "Please use setCurrency(CurrencyUnit) before calling this method!"
            );
        }
        return addPart(title, items, currency);
    }

    public InvoiceBuilder addPart(
            final String title,
            final List<InvoiceItem> items,
            final CurrencyUnit currency
    ) {
        parts.add(
                partBuilder
                        .setTitle(title)
                        .setItems(items)
                        .setCurrency(currency)
                        .build()
        );

        return this;
    }

    public InvoiceBuilder clearParts() {
        parts.clear();
        return this;
    }

    public InvoiceBuilder setAttachments(final List<Attachment> attachments) {
        this.attachments.clear();

        if (attachments != null) {
            this.attachments.addAll(attachments);
        }
        return this;
    }

    public InvoiceBuilder clearAttachents() {
        attachments.clear();
        return this;
    }
}
