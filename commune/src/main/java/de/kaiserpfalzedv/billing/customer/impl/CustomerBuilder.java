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

package de.kaiserpfalzedv.billing.customer.impl;

import java.util.UUID;

import de.kaiserpfalzedv.billing.common.EmailAddress;
import de.kaiserpfalzedv.billing.common.impl.NullEmailAddress;
import de.kaiserpfalzedv.billing.customer.Customer;
import org.apache.commons.lang3.builder.Builder;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class CustomerBuilder implements Builder<Customer> {
    private UUID id = UUID.randomUUID();
    private String name;
    private String costReference;
    private EmailAddress contactAddress;
    private EmailAddress billingAddress;


    @Override
    public Customer build() {
        defaults();
        validate();

        try {
            return new CustomerImpl(id, name, costReference, contactAddress, billingAddress);
        } finally {
            reset();
        }
    }

    private void defaults() {
        if (contactAddress == null) {
            contactAddress = NullEmailAddress.INSTANCE;
        }

        if (billingAddress == null) {
            billingAddress = NullEmailAddress.INSTANCE;
        }
    }

    private void validate() {
        if (isBlank(costReference)) {
            throw new IllegalStateException("Can't create a customer without cost reference");
        }

        if (isBlank(name)) {
            throw new IllegalStateException("Can't create a customer without a name");
        }
    }

    private void reset() {
        id = UUID.randomUUID();
        name = null;
        costReference = null;
        billingAddress = null;
        contactAddress = null;
    }


    public CustomerBuilder setId(UUID id) {
        this.id = id;
        return this;
    }

    public CustomerBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CustomerBuilder setCostReference(String costReference) {
        this.costReference = costReference;
        return this;
    }

    public CustomerBuilder setContactAddress(EmailAddress contactAddress) {
        this.contactAddress = contactAddress;
        return this;
    }

    public CustomerBuilder setBillingAddress(EmailAddress billingAddress) {
        this.billingAddress = billingAddress;
        return this;
    }

    public CustomerBuilder copy(final Customer orig) {
        this.id = orig.getId();
        this.name = orig.getName();
        this.costReference = orig.getCostReference();
        this.contactAddress = orig.getContactAddress();
        this.billingAddress = orig.getBillingAddress();

        return this;
    }
}
