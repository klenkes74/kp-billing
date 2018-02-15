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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.money.MonetaryAmount;

import de.kaiserpfalzedv.billing.api.billed.Attachment;
import de.kaiserpfalzedv.billing.api.billed.Invoice;
import de.kaiserpfalzedv.billing.api.billed.InvoicePart;
import de.kaiserpfalzedv.billing.api.common.impl.IdentifiableImpl;
import de.kaiserpfalzedv.billing.api.guided.Customer;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-11
 */
public class InvoiceImpl extends IdentifiableImpl implements Invoice {
    private static final long serialVersionUID = -5431773950873978228L;

    private final Customer customer;
    private final LocalDate invoiceDate;
    private final String invoiceNumber;
    private final MonetaryAmount amount;

    private final ArrayList<InvoicePart> parts = new ArrayList<>();
    private final ArrayList<Attachment> attachments = new ArrayList<>();


    InvoiceImpl(
            final UUID id,
            final String invoiceNumber,
            final LocalDate invoiceDate,
            final Customer customer,
            final MonetaryAmount amount,
            final List<InvoicePart> parts,
            final List<Attachment> attachments
    ) {
        super(id);

        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.customer = customer;

        this.amount = amount;

        if (parts != null) {
            this.parts.addAll(parts);
        }

        if (attachments != null) {
            this.attachments.addAll(attachments);
        }
    }


    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    @Override
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    @Override
    public MonetaryAmount getAmount() {
        return amount;
    }

    @Override
    public List<InvoicePart> getParts() {
        return Collections.unmodifiableList(parts);
    }

    @Override
    public List<Attachment> getAttachments() {
        return Collections.unmodifiableList(attachments);
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("customer", customer.getName())
                .append("cost reference", customer.getCostReference())
                .append("invoiceDate", invoiceDate)
                .append("invoiceNumber", invoiceNumber)
                .append("amount", amount)
                .toString();
    }
}
