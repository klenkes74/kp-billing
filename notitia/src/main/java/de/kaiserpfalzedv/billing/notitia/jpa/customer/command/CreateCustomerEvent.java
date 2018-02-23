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

package de.kaiserpfalzedv.billing.notitia.jpa.customer.command;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.notitia.api.customer.CreateCustomerCommand;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerTO;
import de.kaiserpfalzedv.billing.notitia.api.customer.EmailAddressTO;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-21
 */
@Entity
@Table(name = "CUSTOMER_CREATE_EVENTS")
@DiscriminatorValue("CREATE")
public class CreateCustomerEvent extends BaseCustomerEvent {
    private static final long serialVersionUID = 3164473870615136023L;

    @Column(name = "NAME_")
    private String name;
    @Column(name = "COST_CENTER_")
    private String costCenter;

    @Column(name = "BILLING_ID_")
    private UUID billingAddressId;
    @Column(name = "BILLING_NAME_")
    private String billingAddressName;
    @Column(name = "BILLING_ADDRESS_")
    private String billingAddressAddress;
    @Column(name = "BILLING_TYPE_")
    private String billingAddressType;

    @Column(name = "CONTACT_ID_")
    private UUID contactAddressId;
    @Column(name = "CONTACT_NAME_")
    private String contactAddressName;
    @Column(name = "CONTACT_ADDRESS_")
    private String contactAddressAddress;
    @Column(name = "CONTACT_TYPE_")
    private String contactAddressType;


    @ElementCollection
    @MapKeyColumn(name="KEY_")
    @Column(name="VALUE_")
    @CollectionTable(name="CUSTOMER_CREATE_TAGS", joinColumns=@JoinColumn(name="CUSTOMER_"))
    private Map<String, String> tags = new HashMap<>(); // maps from attribute name to value


    @SuppressWarnings("deprecation")
    @Deprecated
    public CreateCustomerEvent() {}

    public CreateCustomerEvent(@NotNull final CreateCustomerCommand command, @NotNull final OffsetDateTime executed) {
        super(command.getId(), command.getObjectId(), command.getCreated(), executed);

        CustomerTO data = command.getData();
        
        setCustomerBaseData(data);
        setBillingAddress(data.getBillingAddress());
        setContactAddress(data.getContactAddress());
    }

    private void setCustomerBaseData(CustomerTO data) {
        name = data.getName();
        costCenter = data.getCostCenter();
        tags.putAll(data.getTags());
    }

    private void setBillingAddress(@NotNull final EmailAddressTO email) {
        billingAddressId = email.getId();
        billingAddressName = email.getName();
        billingAddressAddress = email.getAddress();
        billingAddressType = email.getType();
    }

    public void setContactAddress(@NotNull final EmailAddressTO email) {
        contactAddressId = email.getId();
        contactAddressName = email.getName();
        contactAddressAddress = email.getAddress();
        contactAddressType = email.getType();
    }


    @Override
    public CustomerTO update(@NotNull CustomerTO data) {
        data.setId(getObjectId());
        data.setName(getName());
        data.setCostCenter(getCostCenter());
        data.setBillingAddress(generateEmailAddressTO(billingAddressId, billingAddressName, billingAddressAddress, billingAddressName));
        data.setContactAddress(generateEmailAddressTO(contactAddressId, contactAddressName, contactAddressAddress, contactAddressName));
        data.setTags(getTags());

        BUSINESS.trace("Command {} created customer on {} and filled with data: {}",
                       getId(), getExecuted(), data);
        return data;
    }

    private EmailAddressTO generateEmailAddressTO(
            @NotNull final UUID id,
            @NotNull final String name,
            @NotNull final String address,
            @NotNull final String type
    ) {
        return new EmailAddressTO(id, name, address, type);
    }


    public String getName() {
        return name;
    }

    public void setName(@NotNull final String name) {
        this.name = name;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(@NotNull final String costCenter) {
        this.costCenter = costCenter;
    }

    public UUID getBillingAddressId() {
        return billingAddressId;
    }

    public void setBillingAddressId(@NotNull final UUID billingAddressId) {
        this.billingAddressId = billingAddressId;
    }

    public String getBillingAddressName() {
        return billingAddressName;
    }

    public void setBillingAddressName(@NotNull final String billingAddressName) {
        this.billingAddressName = billingAddressName;
    }

    public String getBillingAddressAddress() {
        return billingAddressAddress;
    }

    public void setBillingAddressAddress(@NotNull final String billingAddressAddress) {
        this.billingAddressAddress = billingAddressAddress;
    }

    public String getBillingAddressType() {
        return billingAddressType;
    }

    public void setBillingAddressType(@NotNull final String billingAddressType) {
        this.billingAddressType = billingAddressType;
    }

    public UUID getContactAddressId() {
        return contactAddressId;
    }

    public void setContactAddressId(@NotNull final UUID contactAddressId) {
        this.contactAddressId = contactAddressId;
    }

    public String getContactAddressName() {
        return contactAddressName;
    }

    public void setContactAddressName(@NotNull final String contactAddressName) {
        this.contactAddressName = contactAddressName;
    }

    public String getContactAddressAddress() {
        return contactAddressAddress;
    }

    public void setContactAddressAddress(@NotNull final String contactAddressAddress) {
        this.contactAddressAddress = contactAddressAddress;
    }

    public String getContactAddressType() {
        return contactAddressType;
    }

    public void setContactAddressType(@NotNull final String contactAddressType) {
        this.contactAddressType = contactAddressType;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(@NotNull final Map<String, String> tags) {
        this.tags = tags;
    }
}
