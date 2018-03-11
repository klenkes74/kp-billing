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
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerTO;
import de.kaiserpfalzedv.billing.notitia.api.customer.EmailAddressTO;
import de.kaiserpfalzedv.billing.notitia.api.customer.UpdateCustomerBillingEmailCommand;

import static java.time.ZoneOffset.UTC;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-21
 */
@Entity
@Table(name = "CUSTOMER_UPDATE_BILLING_EMAIL_EVENTS")
@DiscriminatorValue("CREATE")
public class UpdateCustomerBillingEmailEvent extends BaseCustomerEvent {
    private static final long serialVersionUID = -3939177420686685939L;

    @Column(name = "ADDRESS_ID_")
    private UUID addressId;
    @Column(name = "ADDRESS_NAME_", length = 200)
    private String addressName;
    @Column(name = "ADDRESS_ADDRESS_", length = 100)
    private String addressAddress;
    @Column(name = "ADDRESS_TYPE_", length = 20)
    private String addressType;

    @SuppressWarnings("deprecation")
    @Deprecated
    public UpdateCustomerBillingEmailEvent() {}

    public UpdateCustomerBillingEmailEvent(@NotNull final UpdateCustomerBillingEmailCommand command) {
        this(command, OffsetDateTime.now(UTC));
    }

    public UpdateCustomerBillingEmailEvent(@NotNull final UpdateCustomerBillingEmailCommand command, @NotNull final OffsetDateTime executed) {
        super(command.getId(), command.getObjectId(), command.getCreated(), executed);

        EmailAddressTO data = command.getEmailAddress();
        addressId = data.getId();
        addressName = data.getName();
        addressAddress = data.getAddress();
        addressType = data.getType();
    }


    @Override
    public CustomerTO update(@NotNull CustomerTO data) {
        EmailAddressTO email = generateEmailAddressTO(addressId, addressName, addressAddress, addressName);

        BUSINESS.trace("Command {} changed customer on {} and changed the billing address: {} -> {}",
                       getId(), getExecuted(), data.getBillingAddress(), email);

        data.setBillingAddress(email);
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


    public UUID getAddressId() {
        return addressId;
    }

    public void setAddressId(@NotNull final UUID addressId) {
        this.addressId = addressId;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(@NotNull final String addressName) {
        this.addressName = addressName;
    }

    public String getAddressAddress() {
        return addressAddress;
    }

    public void setAddressAddress(@NotNull final String addressAddress) {
        this.addressAddress = addressAddress;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(@NotNull final String addressType) {
        this.addressType = addressType;
    }

}
