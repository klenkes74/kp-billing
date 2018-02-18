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

package de.kaiserpfalzedv.billing.notitia.customer;

import java.io.Serializable;
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

import de.kaiserpfalzedv.billing.api.common.EmailAddress;
import de.kaiserpfalzedv.billing.api.guided.Customer;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-17
 */
@Entity(name = "Customers")
@Table(name = "CUSTOMERS")
@Cacheable
public class JPACustomer extends JPAIdentifiable implements Customer, Serializable {
    private static final long serialVersionUID = 2464687007351883145L;

    @Column(name = "NAME", length=100, nullable = false)
    private String name;

    @Column(name = "COST_REFERENCE", length=200, nullable = false)
    private String costReference;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @Column(name = "CONTACT_ADDRESS")
    private JPAEmailAddress contactAddress;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @Column(name = "BILLING_ADDRESS")
    private JPAEmailAddress billingAddress;

    @ElementCollection
    @MapKeyColumn(name="KEY")
    @Column(name="VALUE")
    @CollectionTable(name="CUSTOMER_TAGS", joinColumns=@JoinColumn(name="CUSTOMER"))
    Map<String, String> tags = new HashMap<>(); // maps from attribute name to value



    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    public String getCostReference() {
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
        return tags;
    }

    public void setTags(final Map<String, String> tags) {
        this.tags = tags;
    }
}
