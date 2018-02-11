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

import java.util.Objects;
import java.util.UUID;

import de.kaiserpfalzedv.billing.common.EmailAddress;
import de.kaiserpfalzedv.billing.guided.Customer;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class CustomerImpl implements Customer {
    private static final long serialVersionUID = -2161820164580456028L;


    private final UUID id;
    private final String name;
    private final String costReference;
    private final EmailAddress contactAddress;
    private final EmailAddress billingAddress;


    CustomerImpl(
            final UUID id,
            final String name,
            final String costReference,
            final EmailAddress contactAddress,
            final EmailAddress billingAddress
    ) {
        this.id = id;
        this.name = name;
        this.costReference = costReference;
        this.contactAddress = contactAddress;
        this.billingAddress = billingAddress;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerImpl)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(getId(), customer.getId());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(System.identityHashCode(this))
                .append("id", id)
                .append("name", name)
                .append("costReference", costReference)
                .append("contactAddress", contactAddress)
                .append("billingAddress", billingAddress)
                .toString();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCostReference() {
        return costReference;
    }

    @Override
    public EmailAddress getContactAddress() {
        return contactAddress;
    }

    @Override
    public EmailAddress getBillingAddress() {
        return billingAddress;
    }
}
