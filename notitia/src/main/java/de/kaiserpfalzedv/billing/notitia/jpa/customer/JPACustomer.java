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

package de.kaiserpfalzedv.billing.notitia.jpa.customer;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.api.common.EmailAddress;
import de.kaiserpfalzedv.billing.api.guided.Customer;
import de.kaiserpfalzedv.billing.notitia.jpa.JPAIdentifiable;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-17
 */
@Entity(name = "Customers")
@Table(name = "CUSTOMERS")
@Cacheable
public class JPACustomer extends JPAIdentifiable implements Customer, Serializable {
    private static final long serialVersionUID = -4162491450493912448L;

    @Column(name = "NAME_", length=200, nullable = false)
    private String name;

    @Column(name = "COST_REFERENCE_", length=100, nullable = false)
    private String costReference;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "CONTACT_ADDRESS_")
    private JPAEmailAddress contactAddress;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "BILLING_ADDRESS_")
    private JPAEmailAddress billingAddress;

    @ElementCollection
    @MapKeyColumn(name="KEY_")
    @Column(name="VALUE_")
    @CollectionTable(name="CUSTOMER_TAGS", joinColumns=@JoinColumn(name="CUSTOMER_"))
    private Map<String, String> tags = new HashMap<>(); // maps from attribute name to value


    public JPACustomer() {}

    public JPACustomer(@NotNull final Customer orig) {
        setId(orig.getId());
        setName(orig.getName());
        setCostReference(orig.getCostCenter());
        setContactAddress(new JPAEmailAddress(orig.getContactAddress()));
        setBillingAddress(new JPAEmailAddress(orig.getBillingAddress()));
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    public String getCostCenter() {
        return costReference;
    }

    public void setCostReference(final String costReference) {
        this.costReference = costReference;
    }
    

    public EmailAddress getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(final JPAEmailAddress contactAddress) {
        this.contactAddress = contactAddress;
    }

    public EmailAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(final JPAEmailAddress billingAddress) {
        this.billingAddress = billingAddress;
    }


    public Map<String, String> getTags() {
        return Collections.unmodifiableMap(tags);
    }

    public void setTags(@NotNull final Map<String, String> tags) {
        if (this.tags != null) {
            this.tags.clear();
        } else {
            this.tags = new HashMap<>(tags.size());
        }

        this.tags.putAll(tags);
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("name", name)
                .append("costReference", costReference)
                .append("contactAddress", contactAddress)
                .append("billingAddress", billingAddress)
                .append("tags", tags)
                .toString();
    }
}
