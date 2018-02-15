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

import java.util.Objects;
import java.util.UUID;

import javax.xml.registry.JAXRException;

import de.kaiserpfalzedv.billing.api.common.EmailAddress;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class EmailAddressImpl implements EmailAddress {
    private static final long serialVersionUID = -5444452028386854502L;

    private final UUID id;
    private final String name;
    private final String address;
    private final String type;


    EmailAddressImpl(
            final UUID id,
            final String name,
            final String address,
            final String type
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.type = type;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) throws JAXRException {
        throw new UnsupportedOperationException("Sorry, this object is inmutable.");
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) throws JAXRException {
        throw new UnsupportedOperationException("Sorry, this object is inmutable.");
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmailAddress)) return false;

        EmailAddress that = (EmailAddress) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(System.identityHashCode(this))
                .append("id", id)
                .append("name", name)
                .append("address", address)
                .append("type", type)
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
}
