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

import java.util.UUID;

import javax.money.MonetaryAmount;

import de.kaiserpfalzedv.billing.api.billed.InvoiceItem;
import de.kaiserpfalzedv.billing.api.common.impl.IdentifiableImpl;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-12
 */
public class InvoiceItemImpl extends IdentifiableImpl implements InvoiceItem {
    private static final long serialVersionUID = -378736993390859708L;

    private final String itemId;
    private final String title;
    private final MonetaryAmount amount;

    public InvoiceItemImpl(
            final UUID id,
            final String itemId,
            final String title,
            final MonetaryAmount amount
    ) {
        super(id);
        this.itemId = itemId;
        this.title = title;
        this.amount = amount;
    }


    @Override
    public String getItemId() {
        return itemId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public MonetaryAmount getAmount() {
        return amount;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("itemId", itemId)
                .append("title", title)
                .append("amount", amount)
                .toString();
    }
}
