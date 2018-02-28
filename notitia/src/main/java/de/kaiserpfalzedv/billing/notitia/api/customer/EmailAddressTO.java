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

package de.kaiserpfalzedv.billing.notitia.api.customer;

import java.util.Objects;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.api.common.EmailAddress;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-22
 */
public class EmailAddressTO implements EmailAddress {
    private static final long serialVersionUID = -5407361548910643020L;


    private UUID id;
    private String name;
    private String address;
    private String type;

    public EmailAddressTO() {}

    public EmailAddressTO(final EmailAddress orig) {
        setId(orig.getId());
        setName(orig.getName());
        setAddress(orig.getAddress());
        setType(orig.getType());
    }

    public EmailAddressTO(@NotNull final UUID id, @NotNull final String name,
                          @NotNull final String address, @NotNull final String type) {
        setId(id);
        setName(name);
        setAddress(address);
        setType(type);
    }

    
    @Override
    public UUID getId() {
        return id;
    }

    public void setId(@NotNull final UUID id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(@NotNull final String name) {
        this.name = name;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(@NotNull final String address) {
        this.address = address;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(@NotNull final String type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmailAddressTO)) return false;
        EmailAddressTO that = (EmailAddressTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("address", address)
                .toString();
    }
}
