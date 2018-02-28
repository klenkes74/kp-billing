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

package de.kaiserpfalzedv.billing.api.common.impl;

import java.util.UUID;

import de.kaiserpfalzedv.billing.api.common.EmailAddress;
import org.apache.commons.lang3.builder.Builder;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class EmailAddressBuilder implements Builder<EmailAddress> {
    public static final String CONTACT_EMAIL = "contact";
    public static final String BILLING_EMAIL = "billing";

    private UUID id = UUID.randomUUID();
    private String name = "";
    private String address;
    private String type = CONTACT_EMAIL;


    @Override
    public EmailAddress build() {
        validate();

        try {
            return new EmailAddressImpl(id, name, address, type);
        } finally {
            reset();
        }
    }

    private void validate() {
        if (isBlank(address)) {
            throw new IllegalStateException("Can't create a mail address without address");
        }
    }

    public void reset() {
        id = UUID.randomUUID();
        name = "";
        address = null;
        type = CONTACT_EMAIL;
    }


    public EmailAddressBuilder setId(UUID id) {
        this.id = id;
        return this;
    }

    public EmailAddressBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public EmailAddressBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public EmailAddressBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public EmailAddressBuilder billingAddress() {
        this.type = BILLING_EMAIL;
        return this;
    }

    public EmailAddressBuilder contactAddress() {
        this.type = CONTACT_EMAIL;
        return this;
    }

    public EmailAddressBuilder copy(final EmailAddress orig) {
        this.id = orig.getId();
        this.address = orig.getAddress();
        this.name = orig.getName();
        this.type = orig.getType();

        return this;
    }
}
