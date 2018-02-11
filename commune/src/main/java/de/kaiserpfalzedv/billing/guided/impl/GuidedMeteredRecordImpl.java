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

package de.kaiserpfalzedv.billing.guided.impl;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import de.kaiserpfalzedv.billing.base.impl.BaseMeteredBillingRecordImpl;
import de.kaiserpfalzedv.billing.common.EmailAddress;
import de.kaiserpfalzedv.billing.guided.Customer;
import de.kaiserpfalzedv.billing.guided.GuidedMeteredRecord;
import de.kaiserpfalzedv.billing.guided.ProductRecordInfo;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-11
 */
public class GuidedMeteredRecordImpl extends BaseMeteredBillingRecordImpl implements GuidedMeteredRecord {
    private static final long serialVersionUID = -8115374713374408753L;

    private final Customer customer;
    private final ProductRecordInfo productInfo;

    public GuidedMeteredRecordImpl(
            final UUID id,
            final String meteringId,
            final Customer customer,
            final OffsetDateTime recordedDate,
            final OffsetDateTime importedDate,
            final OffsetDateTime valueDate,
            final ProductRecordInfo productInfo,
            final BigDecimal meteredValue
    ) {
        super(id, meteringId, recordedDate, importedDate, valueDate, meteredValue);

        this.customer = customer;
        this.productInfo = productInfo;
    }


    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public String getCustomerName() {
        return customer.getName();
    }

    @Override
    public EmailAddress getBillingAddress() {
        return customer.getBillingAddress();
    }

    @Override
    public EmailAddress getContactAddress() {
        return customer.getContactAddress();
    }


    @Override
    public ProductRecordInfo getProductInfo() {
        return productInfo;
    }

    @Override
    public String getProductName() {
        return productInfo.getProductName();
    }

    @Override
    public String[] getTagTitles() {
        return productInfo.getTagTitles();
    }

    @Override
    public String[] getTags() {
        return productInfo.getTags();
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("customer", customer)
                .append("productInfo", productInfo)
                .toString();
    }
}
