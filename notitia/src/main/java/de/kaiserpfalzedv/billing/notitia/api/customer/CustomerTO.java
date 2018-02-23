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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import de.kaiserpfalzedv.billing.api.guided.Customer;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-22
 */
public class CustomerTO implements Customer {
    private static final long serialVersionUID = -6339990140209651812L;

    private String id;
    @XmlTransient
    private UUID uuid;
    private String name;
    private String costCenter;
    private EmailAddressTO contactAddress;
    private EmailAddressTO billingAddress;

    private HashMap<String, String> tags = new HashMap<>();


    public CustomerTO() {}

    public CustomerTO(final Customer orig) {
        setId(orig.getId());
        setName(orig.getName());
        setCostCenter(orig.getCostCenter());

        setContactAddress(new EmailAddressTO(orig.getContactAddress()));
        setBillingAddress(new EmailAddressTO(orig.getBillingAddress()));
    }

    @Override
    public UUID getId() {
        if (uuid == null) {
            uuid = UUID.fromString(id);
        }
        return uuid;
    }

    public void setId(@NotNull final UUID id) {
        this.uuid = id;
        this.id = id.toString();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(@NotNull final String name) {
        this.name = name;
    }

    @Override
    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(@NotNull final String costCenter) {
        this.costCenter = costCenter;
    }

    @Override
    public EmailAddressTO getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(@NotNull final EmailAddressTO contactAddress) {
        this.contactAddress = contactAddress;
    }

    @Override
    public EmailAddressTO getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(@NotNull final EmailAddressTO billingAddress) {
        this.billingAddress = billingAddress;
    }

    @Override
    public Map<String, String> getTags() {
        return Collections.unmodifiableMap(tags);
    }

    public void setTags(@NotNull final Map<String, String> tags) {
        this.tags.clear();

        if (tags != null) {
            this.tags.putAll(tags);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerTO)) return false;
        CustomerTO that = (CustomerTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("uuid", uuid)
                .append("name", name)
                .append("costCenter", costCenter)
                .append("contactAddress", contactAddress)
                .append("billingAddress", billingAddress)
                .append("tags", tags)
                .toString();
    }
}
