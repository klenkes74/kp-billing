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
import java.time.ZoneOffset;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerDeleteCommand;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerTO;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-21
 */
@Entity
@Table(name = "CUSTOMER_DELETE_EVENTS")
@DiscriminatorValue("DELETE")
public class JPACustomerDeleteEvent extends JPACustomerEvent {
    private static final long serialVersionUID = -8470705693153760280L;

    @SuppressWarnings("deprecation")
    @Deprecated
    public JPACustomerDeleteEvent() {}

    public JPACustomerDeleteEvent(@NotNull final CustomerDeleteCommand command) {
        this(command, OffsetDateTime.now(ZoneOffset.UTC));
    }

    @SuppressWarnings("WeakerAccess")
    public JPACustomerDeleteEvent(@NotNull final CustomerDeleteCommand command, @NotNull final OffsetDateTime executed) {
        super(command.getId(), command.getObjectId(), command.getCreated(), executed);
    }


    @Override
    public CustomerTO update(@NotNull CustomerTO data) {
        BUSINESS.trace("Command {} deleted customer {} on {}.", getId(), data.getId(), getExecuted());
        
        return new CustomerTO();
    }
}
