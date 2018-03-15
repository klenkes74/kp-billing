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

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerTO;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerUpdateNameCommand;

import static java.time.ZoneOffset.UTC;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-21
 */
@Entity
@Table(name = "CUSTOMER_UPDAte_NAME_EVENTS")
@DiscriminatorValue("UPDATE_NAME")
public class JPACustomerUpdateNameEvent extends JPACustomerEvent {
    private static final long serialVersionUID = -9108063307952169028L;

    @Column(name = "NAME_")
    private String name;

    @SuppressWarnings("deprecation")
    @Deprecated
    public JPACustomerUpdateNameEvent() {}


    public JPACustomerUpdateNameEvent(@NotNull final CustomerUpdateNameCommand command) {
        this(command, OffsetDateTime.now(UTC));
    }


    public JPACustomerUpdateNameEvent(@NotNull final CustomerUpdateNameCommand command, @NotNull final OffsetDateTime executed) {
        super(command.getId(), command.getObjectId(), command.getCreated(), executed);

        name = command.getCustomerName();
    }

    @Override
    public CustomerTO update(@NotNull CustomerTO data) {
        BUSINESS.trace("Command {} updated customer on {} and changed name: {} -> {}",
                       getId(), getExecuted(), data.getName(), getName());

        data.setName(getName());

        return data;
    }


    public String getName() {
        return name;
    }

    public void setName(@NotNull final String name) {
        this.name = name;
    }
}