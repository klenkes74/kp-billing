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

package de.kaiserpfalzedv.billing.api.billed;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.money.MonetaryAmount;

import de.kaiserpfalzedv.billing.api.common.Identifiable;
import de.kaiserpfalzedv.billing.api.guided.Customer;

/**
 * This is the invoice for a single customer. It has some header data about the timespan, the customer of the invoice.
 * And it contains of multiple {@link InvoicePart}s containing the real {@link InvoiceItem}s.
 * <p>
 * So the data can be structured by the generator. Currently there is special handling of taxes and fees. But using
 * the {@link InvoicePart} mechanism the concrete implemntation may take care of them.
 *
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-11
 */
public interface Invoice extends Identifiable, Serializable {
    /**
     * @return The customer receiving this invoice.
     */
    Customer getCustomer();


    /**
     * @return The date of the invoice.
     */
    LocalDate getInvoiceDate();

    /**
     * @return The customer facing number of the invoice (defaults to the UUID presented by {@link #getId()}).
     */
    String getInvoiceNumber();

    /**
     * @return The bottom line number of the invoice. The sum of all parts of the invoice (as returned by
     * {@link #getParts()}).
     */
    MonetaryAmount getAmount();


    /**
     * @return The unmodifiable parts of the invoice. Containing the {@link InvoiceItem}s of the invoice.
     */
    List<InvoicePart> getParts();

    /**
     * @return Unmodifiable list of additional documents as attachments of the invoice.
     */
    List<Attachment> getAttachments();
}
